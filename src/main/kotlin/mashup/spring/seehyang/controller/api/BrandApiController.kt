package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.perfume.BrandEditRequest
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.service.BrandService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@ApiV1
class BrandApiController(
    val brandService: BrandService
) {

    @PutMapping("/brand/{id}")
    fun saveBrand(
        @PathVariable id: Long,
        @RequestBody brandEditRequest: BrandEditRequest
    ): SeehyangResponse<String> {
        brandService.edit(id, brandEditRequest)
        return SeehyangResponse("OK")
    }
}