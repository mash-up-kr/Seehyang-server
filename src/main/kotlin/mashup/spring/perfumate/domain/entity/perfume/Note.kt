package mashup.spring.perfumate.domain.entity.perfume

import javax.persistence.*

@Entity
class Note(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val koreanName: String,
    val type: NoteType
)