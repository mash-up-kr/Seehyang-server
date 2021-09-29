package mashup.spring.perfumate.controller.api.dto.perfume

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.perfumate.domain.entity.perfume.Accord

data class AccordDto(
    @JsonIgnore
    val accord: Accord,
    val id: Long? = accord.id,
    val name: String = accord.name,
    val koreanName: String = accord.koreanName
) {
    override fun toString(): String {
        return "AccordDto(id=$id, name=$name, koreanName=$koreanName)"
    }
}