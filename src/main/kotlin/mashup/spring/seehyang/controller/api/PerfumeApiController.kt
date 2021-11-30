package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.perfume.BasicPerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeEditRequest
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.service.PerfumeService
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class PerfumeApiController(
    private val perfumeService: PerfumeService
) {

    @GetMapping("/perfume/{perfumeId}")
    fun getPerfumeDetail(
        @PathVariable perfumeId: Long,
        @ApiIgnore userDto: UserDto
    ) : SeehyangResponse<PerfumeDto> {

        val perfume = perfumeService.getPerfume(userDto, perfumeId)

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

    @PostMapping("/perfume/{perfumeId}/like")
    fun likePerfume(
        @PathVariable perfumeId: Long,
        @ApiIgnore userDto: UserDto
    ): SeehyangResponse<Map<String, Boolean>> {

        val isLiked = perfumeService.likePerfume(userDto, perfumeId)

        return SeehyangResponse(mutableMapOf(Pair("isLiked", isLiked)))
    }

    @PutMapping("/perfume/{perfumeId}")
    fun savePerfume(
        @PathVariable perfumeId: Long,
        @RequestBody perfumeEditRequest: PerfumeEditRequest
    ): SeehyangResponse<String> {

        perfumeService.editPerfume(perfumeId, perfumeEditRequest)

        return SeehyangResponse("OK")
    }
}