package mashup.spring.seehyang.controller.api

import io.swagger.annotations.ApiParam
import mashup.spring.seehyang.controller.api.dto.perfume.BasicPerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeEditRequest
import mashup.spring.seehyang.service.PerfumeService
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.UnauthorizedException
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class PerfumeApiController(
    private val perfumeService: PerfumeService
) {

    @GetMapping("/perfume/{id}")
    fun getPerfumeDetail(
        @PathVariable id: Long,
        @ApiIgnore user: User
    ) : SeehyangResponse<PerfumeDto> {
        val perfume = perfumeService.get(user,id)
        return SeehyangResponse(perfume)
    }

    @GetMapping("/perfume/list")
    fun getPerfumeByName(
        @RequestParam(value = "name") name: String,
        @RequestParam(value = "cursor", required = false) cursor: Long? = null,
    ): SeehyangResponse<List<BasicPerfumeDto>>{
        val perfumeDtoList = perfumeService.getByName(name,cursor)
        return SeehyangResponse(perfumeDtoList)
    }

    @PostMapping("/perfume/{id}/like")
    fun likePerfume(
        @PathVariable id: Long,
        @ApiIgnore user: User
    ): SeehyangResponse<Map<String, Boolean>> {
        if (user.isLogin().not()) throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)

        val isLiked = perfumeService.likePerfume(user, id)

        return SeehyangResponse(mutableMapOf(Pair("isLiked", isLiked)))
    }

    @PutMapping("/perfume/{id}")
    fun savePerfume(
        @PathVariable id: Long,
        @RequestBody perfumeEditRequest: PerfumeEditRequest
    ): SeehyangResponse<String> {
        perfumeService.edit(id, perfumeEditRequest)
        return SeehyangResponse("OK")
    }
}