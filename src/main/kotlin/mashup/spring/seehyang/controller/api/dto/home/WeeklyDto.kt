package mashup.spring.seehyang.controller.api.dto.home

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.domain.entity.perfume.Perfume

data class WeeklyDto(
    val perfumeEntityList: List<Perfume>,
    val perfumes: List<WeeklyPerfumeDto> = perfumeEntityList.map { WeeklyPerfumeDto(it) }
)

data class WeeklyPerfumeDto(
    @JsonIgnore
    val perfume: Perfume,
    val id: Long = perfume.id!!,
    val perfumeImageUrl: String = perfume.thumbnailUrl,
    val brandName: String = perfume.brand.koreanName,
    val perfumeName: String = perfume.koreanName
)