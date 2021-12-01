package mashup.spring.seehyang.controller.api.dto.perfume

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.domain.entity.perfume.Brand

data class BrandDto(

    @JsonIgnore
    val brand: Brand,
    val id: Long? = brand.id,
    val name: String = brand.name,
    val koreanName:String = brand.koreanName,
    val perfumes: List<PerfumeDto> = brand.perfumes.map { PerfumeDto(it) }

) {
}