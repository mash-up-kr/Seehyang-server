package mashup.spring.perfumate.controller.api

import mashup.spring.perfumate.controller.dto.perfume.PerfumeDto
import mashup.spring.perfumate.service.PerfumeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@ApiV1
class PerfumeApiController(
    private val perfumeService: PerfumeService
) {

    @GetMapping("/perfume/{id}")
    fun getPerfumeDetail(
        @PathVariable id: Long
    ) : PerfumeDto {
        val perfume = perfumeService.get(id)
        return PerfumeDto(perfume)
    }
}