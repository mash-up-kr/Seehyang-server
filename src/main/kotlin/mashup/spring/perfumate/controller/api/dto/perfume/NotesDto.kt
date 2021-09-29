package mashup.spring.perfumate.controller.api.dto.perfume

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.perfumate.domain.entity.perfume.NoteType
import mashup.spring.perfumate.domain.entity.perfume.Perfume

data class NotesDto(
    @JsonIgnore
    val perfume: Perfume,
    val top: MutableList<NoteDto> = mutableListOf(),
    val middle: MutableList<NoteDto> = mutableListOf(),
    val base: MutableList<NoteDto> = mutableListOf(),
    val default: MutableList<NoteDto> = mutableListOf()

) {
    init {
        perfume.notes.forEach {
            when(it.note.type) {
                NoteType.TOP -> top.add(NoteDto(it.note))
                NoteType.MIDDLE -> middle.add(NoteDto(it.note))
                NoteType.BASE -> base.add(NoteDto(it.note))
                else -> default.add(NoteDto(it.note))
            }
        }
    }

    override fun toString(): String {
        return "NotesDto(top=$top, middle=$middle, base=$base, default=$default)"
    }
}