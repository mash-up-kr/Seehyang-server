package mashup.spring.seehyang.domain.entity.perfume

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import javax.persistence.*

@Entity
class PerfumeNote(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "perfume_id")
    val perfume: Perfume,
    @ManyToOne
    @JoinColumn(name = "note_id")
    val note: Note
) : BaseTimeEntity()