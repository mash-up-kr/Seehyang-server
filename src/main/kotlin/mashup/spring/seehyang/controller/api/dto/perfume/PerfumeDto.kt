package mashup.spring.seehyang.controller.api.dto.perfume

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.controller.api.dto.addBucketUrl
import mashup.spring.seehyang.domain.entity.perfume.Perfume

data class PerfumeDto(
    @JsonIgnore
    val perfume: Perfume,
    val id: Long? = perfume.id,
    val name: String = perfume.name,
    val koreanName: String = perfume.koreanName,
    val thumbnailUrl: String = addBucketUrl(perfume.thumbnailUrl),
    val brandId: Long? = perfume.brand.id,
    val brandName: String = perfume.brand.koreanName,
    val notes: NotesDto = NotesDto(perfume),
    val accords: MutableList<AccordDto> = mutableListOf(),
    val isLiked: Boolean = false,
    val likeCount: Int = perfume.likeCount

) {
    init {
        perfume.viewAccords().forEach { accords.add(AccordDto(it.accord)) }
    }

    override fun toString(): String {
        return "PerfumeDto(" +
                "id=$id, " +
                "name=$name, " +
                "thumbnailUrl=$thumbnailUrl, " +
                "brandId=$brandId, " +
                "brandName=$brandName, " +
                "koreanName=$koreanName, " +
                "notes=$notes)"
    }
}
