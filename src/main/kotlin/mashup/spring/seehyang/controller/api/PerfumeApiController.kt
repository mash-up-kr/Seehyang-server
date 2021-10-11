package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeEditRequest
import mashup.spring.seehyang.service.PerfumeService
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import org.springframework.web.bind.annotation.*

@ApiV1
class PerfumeApiController(
    private val perfumeService: PerfumeService
) {

    @GetMapping("/perfume/{id}")
    fun getPerfumeDetail(
        @PathVariable id: Long
    ) : SeehyangResponse<PerfumeDto> {
        val perfume = perfumeService.get(id)
        return SeehyangResponse(PerfumeDto(perfume))
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