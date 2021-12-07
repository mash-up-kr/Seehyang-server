package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.perfume.BasicPerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeEditRequest
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.service.PerfumeService
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.service.auth.UserId
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class PerfumeApiController(
    private val perfumeService: PerfumeService
) {

    @GetMapping("/perfume/{perfumeId}")
    fun getPerfumeDetail(
        @PathVariable perfumeId: Long,
        @ApiIgnore userId: UserId?
    ) : SeehyangResponse<PerfumeDto> {

        val perfume = perfumeService.getPerfume(perfumeId,userId)

        return SeehyangResponse(perfume)
    }

    /**
     * 향수 id로 특정 시점의 스토리 개수가 몇개인지 조회
     * https://github.com/mash-up-kr/Seehyang-server/issues/67
     */
    @GetMapping("/perfume/{perfumeId}/storyCount")
    fun getStoryCount(
        @PathVariable perfumeId: Long,
        @ApiIgnore userId: UserId?
    ): SeehyangResponse<Long>{
        val count = perfumeService.getStoryCount(perfumeId, userId)

        return SeehyangResponse(count)
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
        @ApiIgnore userId: UserId?
    ): SeehyangResponse<Map<String, Boolean>> {

        if(userId == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val isLiked = perfumeService.likePerfume(userId, perfumeId)

        return SeehyangResponse(mutableMapOf(Pair("isLiked", isLiked)))
    }

    @PutMapping("/perfume/{perfumeId}")
    fun savePerfume(
        @PathVariable perfumeId: Long,
        @RequestBody perfumeEditRequest: PerfumeEditRequest
    ): SeehyangResponse<String> {
        //TODO : Perfume 수정 admin 으로?
        perfumeService.editPerfume(perfumeId, perfumeEditRequest)

        return SeehyangResponse("OK")
    }
}