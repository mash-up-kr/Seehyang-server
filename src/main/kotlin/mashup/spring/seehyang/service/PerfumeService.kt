package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.perfume.BasicPerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeEditRequest
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.PerfumeDomain
import mashup.spring.seehyang.domain.UserDomain
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.perfume.PerfumeLike
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.repository.perfume.PerfumeLikeRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PerfumeService(
    private val perfumeDomain: PerfumeDomain,
    private val userDomain: UserDomain
) {

    @Transactional(readOnly = true)
    fun getPerfume(userDto: UserDto, perfumeId: Long) : PerfumeDto {
        //val perfume = perfumeRepository.findById(id).orElseThrow{NotFoundException(SeehyangStatus.NOT_FOUND_PERFUME)}
        val user = userDomain.getUser(userDto)
        return perfumeDomain.getPerfumeWithUser(perfumeId,user)
    }

    @Transactional(readOnly = true)
    fun getByName(name : String, cursor: Long?) : List<BasicPerfumeDto> {

        return perfumeDomain.searchByName(name, cursor).map{BasicPerfumeDto(it) }

    }

    fun editPerfume(perfumeId: Long, request: PerfumeEditRequest){

        perfumeDomain.editPerfume(perfumeId, request)
    }

    fun likePerfume(userDto: UserDto, perfumeId: Long): Boolean {

        val user = userDomain.getUser(userDto)
        return perfumeDomain.likePerfume(perfumeId, user)

    }
}