package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.perfume.BasicPerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeEditRequest
import mashup.spring.seehyang.service.PerfumeService
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.domain.entity.user.User
import org.springframework.web.bind.annotation.*

@ApiV1
class PerfumeApiController(
    private val perfumeService: PerfumeService
) {

    @GetMapping("/perfume/{id}")
    fun getPerfumeDetail(
        @PathVariable id: Long,
        user: User
    ) : SeehyangResponse<PerfumeDto> {
        val perfume = perfumeService.get(id)
        val liked = perfume.perfumeLikes.stream().filter { it.user.id == user.id }.findFirst().isPresent
        return SeehyangResponse(PerfumeDto(perfume, isLiked = liked))
    }

    @GetMapping("/perfume/list")
    fun getPerfumeByName(
        @RequestParam(value = "name") name: String,
        @RequestParam(value = "cursor") cursor: Long? = null,
    ): SeehyangResponse<List<BasicPerfumeDto>>{
        val perfumeDtoList = perfumeService.getByName(name,cursor).map{BasicPerfumeDto(it)}.toList()
        return SeehyangResponse(perfumeDtoList)
    }

    @PutMapping("/perfume/{id}")
    fun savePerfume(
        @PathVariable id: Long,
        @RequestBody perfumeEditRequest: PerfumeEditRequest
    ): SeehyangResponse<String> {
        perfumeService.edit(id, perfumeEditRequest)
        return SeehyangResponse("OK")
    }

    @PostMapping("/perfume/{id}/like")
    fun likePerfume(
        @PathVariable id: Long,
        user: User
    ): SeehyangResponse<Map<String, Boolean>> {
        if (user.isLogin().not()) throw RuntimeException("Not Authorization user..")

        val isLiked = perfumeService.likePerfume(user, id)

        return SeehyangResponse(mutableMapOf(Pair("isLiked", isLiked)))
    }
}