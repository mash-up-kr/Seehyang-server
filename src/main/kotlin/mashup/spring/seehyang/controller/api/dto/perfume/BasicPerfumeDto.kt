package mashup.spring.seehyang.controller.api.dto.perfume

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.controller.api.dto.addBucketUrl
import mashup.spring.seehyang.domain.entity.perfume.Perfume

data class BasicPerfumeDto(
    @JsonIgnore
    val perfume: Perfume,
    val id: Long? = perfume.id,
    val name: String = perfume.name,
    val koreanName: String = perfume.koreanName,
    val thumbnailUrl: String = addBucketUrl(perfume.thumbnailUrl),
    val brandName: String = perfume.brand.koreanName,
    val likeCount: Int = perfume.likeCount
) {
}