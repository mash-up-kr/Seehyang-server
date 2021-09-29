package mashup.spring.perfumate.controller.api.dto.perfume

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.perfumate.domain.entity.perfume.Note

data class NoteDto (
    @JsonIgnore
    val note: Note,
    val id: Long? = note.id,
    val name: String = note.name,
    val koreanName: String = note.koreanName
) {
    override fun toString(): String {
        return "NoteDto(id=$id, name=$name, koreanName=$koreanName)"
    }
}