package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.perfume.BasicPerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeEditRequest
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.perfume.PerfumeLike
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.repository.perfume.PerfumeLikeRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PerfumeService(
    private val perfumeRepository: PerfumeRepository,
    private val perfumeLikeRepository: PerfumeLikeRepository,
    private val userService: UserService
) {

    private val PAGE_SIZE: Int = 10

    @Transactional(readOnly = true)
    fun get(user: User,id: Long) : PerfumeDto {
        val perfume = perfumeRepository.findById(id).orElseThrow{NotFoundException(SeehyangStatus.NOT_FOUND_PERFUME)}

        return if(user.isLogin()){
            val isLiked = perfume.perfumeLikes.stream().filter { it.user.id == user.id }.findFirst().isPresent
            PerfumeDto(perfume, isLiked = isLiked)
        }else{
            PerfumeDto(perfume, isLiked = false)
        }
    }

    @Transactional(readOnly = true)
    fun getByName(name : String, cursor: Long?) : List<BasicPerfumeDto> {
        val perfumes : MutableList<Perfume> = mutableListOf()
        if(cursor == null){
            perfumes.addAll(perfumeRepository.findTop10ByKoreanNameContainsOrderByIdDesc(name))
            perfumes.addAll(perfumeRepository.findTop10ByNameContainsIgnoreCaseOrderByIdDesc(name))
        }else{
            perfumes.addAll(perfumeRepository.findByKoreanName(name, cursor, PageRequest.ofSize(PAGE_SIZE)))
            perfumes.addAll(perfumeRepository.findByEngName(name, cursor, PageRequest.ofSize(PAGE_SIZE)))
        }

        return perfumes.map{ BasicPerfumeDto(it) }
    }

    fun edit(id: Long, request: PerfumeEditRequest): Perfume{
        val perfume = perfumeRepository.findById(id).get()
        // TODO 더 세심한 체크 ..
        perfume.name = request.name!!
        perfume.koreanName = request.koreanName!!
        return perfume
    }

    fun likePerfume(user: User, perfumeId: Long): Boolean {

        val managedUser = userService.getUser(user.id)

        val perfume = perfumeRepository.findById(perfumeId).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_PERFUME) }
        val like = perfumeLikeRepository.findByUserAndPerfume(managedUser, perfume)

        return if (like.isPresent) {
            perfumeLikeRepository.delete(like.get())
            perfume.cancleLike()
            false
        } else {
            perfumeLikeRepository.save(PerfumeLike(user = managedUser, perfume = perfume))
            perfume.like()
            true
        }
    }
}