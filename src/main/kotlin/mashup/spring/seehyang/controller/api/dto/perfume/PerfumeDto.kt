package mashup.spring.seehyang.controller.api.dto.perfume

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.domain.entity.perfume.Perfume

data class PerfumeDto(
    @JsonIgnore
    val perfume: Perfume,
    val id: Long? = perfume.id,
    val name: String = perfume.name,
    val thumbnailUrl: String = perfume.thumbnailUrl,
    val koreanName: String = perfume.koreanName,
    val brandId: Long? = perfume.brand.id,
    val brandName: String = perfume.brand.koreanName,
    val notes: NotesDto = NotesDto(perfume),
    val accords: MutableList<AccordDto> = mutableListOf(),
    val isLiked: Boolean = false
) {
    init {
        perfume.accords.forEach { accords.add(AccordDto(it.accord)) }
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
