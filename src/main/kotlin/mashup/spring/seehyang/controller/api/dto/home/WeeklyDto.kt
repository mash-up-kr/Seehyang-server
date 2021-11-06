package mashup.spring.seehyang.controller.api.dto.home

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.controller.api.dto.addBucketUrl
import mashup.spring.seehyang.domain.entity.perfume.Perfume

data class WeeklyDto(
    @JsonIgnore
    val perfumeEntityList: List<Perfume>,
    val perfumes: List<WeeklyPerfumeDto> = perfumeEntityList.mapIndexed { index, perfume -> WeeklyPerfumeDto(rank = index + 1, perfume = perfume) }
)

data class WeeklyPerfumeDto(
    @JsonIgnore
    val perfume: Perfume,
    val rank: Int,
    val id: Long = perfume.id!!,
    val perfumeImageUrl: String = addBucketUrl(perfume.thumbnailUrl),
    val brandName: String = perfume.brand.koreanName,
    val perfumeName: String = perfume.koreanName
)