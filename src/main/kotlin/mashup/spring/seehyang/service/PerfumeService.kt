package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.perfume.BasicPerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeEditRequest
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.PerfumeDomain
import mashup.spring.seehyang.domain.StoryDomain
import mashup.spring.seehyang.domain.UserDomain
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.service.auth.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PerfumeService(
    private val perfumeDomain: PerfumeDomain,
    private val userDomain: UserDomain,
    private val storyDomain: StoryDomain
) {
    private val UNAUTHORIZED_PERFUME_ACCESS = UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)


    @Transactional(readOnly = true)
    fun getPerfume(perfumeId: Long, userId: UserId?): PerfumeDto {

        var user: User? = null
        if (userId != null) {
            user = userDomain.getLoginUser(userId)
        }

        return perfumeDomain.getPerfumeWithUser(perfumeId, user)
    }

    @Transactional(readOnly = true)
    fun getStoryCount(perfumeId: Long, userId: UserId?): Long{

        var user: User? = null
        if (userId != null) {
            user = userDomain.getLoginUser(userId)
        }
        return storyDomain.countStoriesByPerfume(perfumeId, user)
    }

    @Transactional(readOnly = true)
    fun getByName(name: String, cursor: Long?): List<BasicPerfumeDto> {

        return perfumeDomain.searchByName(name, cursor).map { BasicPerfumeDto(it) }

    }

    fun editPerfume(perfumeId: Long, request: PerfumeEditRequest) {

        perfumeDomain.editPerfume(perfumeId, request)
    }

    fun likePerfume(userId: UserId, perfumeId: Long): Boolean {

        val user = userDomain.getLoginUser(userId)

        return perfumeDomain.likePerfume(perfumeId, user ?: throw UNAUTHORIZED_PERFUME_ACCESS)

    }
}