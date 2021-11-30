package mashup.spring.seehyang.domain

import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeEditRequest
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.domain.enums.Domain
import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.springframework.data.domain.PageRequest

@Domain
class PerfumeDomain(
    private val perfumeRepository: PerfumeRepository
) {

    private val PERFUME_NOT_FOUND_EXCEPTION = NotFoundException(SeehyangStatus.NOT_FOUND_PERFUME)
    private val INVALID_PERFUME_EDIT_EXCEPTION = BadRequestException(SeehyangStatus.INVALID_PERFUME_EDIT_REQUEST)

    private val SEARCH_BY_NAME_PAGE_SIZE: Int = 10
    private val STEADY_PAGE_SIZE: Int = 6


    fun getPerfume(perfumeId: Long): Perfume {
        val perfume = perfumeRepository.findById(perfumeId).orElseThrow { PERFUME_NOT_FOUND_EXCEPTION }
        return perfume
    }

    fun getPerfumes(perfumeIds: List<Long>): List<Perfume>{
        return perfumeRepository.findByIds(perfumeIds)
    }

    fun getPerfumeWithUser(perfumeId: Long, user: User): PerfumeDto {
        val perfume = getPerfume(perfumeId)

        return if (user.isLogin()) {
            val isLiked = perfume.perfumeLikes.any { it.user.id == user.id }
            PerfumeDto(perfume, isLiked = isLiked)
        } else {
            PerfumeDto(perfume, isLiked = false)
        }
    }

    fun getPerfumesWithStoriesMoreThan(todayPerfumeBaseline: Int): List<Perfume> {
        return perfumeRepository.findByStoryLengthGreaterThan(todayPerfumeBaseline)
    }

    fun searchByName(name: String, cursor: Long?): List<Perfume> {

        val perfumes: MutableList<Perfume> = mutableListOf()



        if (cursor == null) {
            perfumes.addAll(perfumeRepository.findTop10ByKoreanNameContainsOrderByIdDesc(name))
            perfumes.addAll(perfumeRepository.findTop10ByNameContainsIgnoreCaseOrderByIdDesc(name))
        } else {
            perfumes.addAll(perfumeRepository.findByKoreanName(name, cursor, PageRequest.ofSize(SEARCH_BY_NAME_PAGE_SIZE)))
            perfumes.addAll(perfumeRepository.findByEngName(name, cursor, PageRequest.ofSize(SEARCH_BY_NAME_PAGE_SIZE)))
        }

        return perfumes

    }

    fun editPerfume(perfumeId: Long, request: PerfumeEditRequest){

        if(request.name == null && request.koreanName == null){
            throw INVALID_PERFUME_EDIT_EXCEPTION
        }

        val perfume = getPerfume(perfumeId)

        perfume.changePerfumeNames(request.name, request.koreanName)
    }


    fun likePerfume(perfumeId:Long, user: User):Boolean{

        val perfume = getPerfume(perfumeId = perfumeId)
        val currentLikeStatus = perfume.likePerfume(user)

        return currentLikeStatus
    }

    fun getSteadyPerfumes(idCursor: Long?, likeCursor: Int?): List<Perfume> {



        return if(idCursor == null && likeCursor == null) {
            perfumeRepository.findTop6ByOrderByLikeCountDescIdDesc()
        }else if(idCursor == null && likeCursor != null){
            throw BadRequestException(SeehyangStatus.INVALID_CURSOR_PARAMETER)
        }else if(idCursor != null && likeCursor == null){
            perfumeRepository.findSteadyPerfume(idCursor, PageRequest.ofSize(STEADY_PAGE_SIZE))
        }
        else {
            perfumeRepository.findSteadyPerfume(idCursor!!, likeCursor!!, PageRequest.ofSize(STEADY_PAGE_SIZE))
        }
    }


}