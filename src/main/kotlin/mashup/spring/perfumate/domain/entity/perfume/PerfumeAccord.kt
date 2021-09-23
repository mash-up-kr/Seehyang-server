package mashup.spring.perfumate.domain.entity.perfume

import mashup.spring.perfumate.domain.entity.BaseTimeEntity
import javax.persistence.*

@Entity
class PerfumeAccord(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "perfume_id")
    val perfume: Perfume,
    @ManyToOne
    @JoinColumn(name = "accord_id")
    val accord: Accord
) : BaseTimeEntity()