package mashup.spring.perfumate.domain.entity.perfume

import mashup.spring.perfumate.domain.entity.BaseTimeEntity
import javax.persistence.*

@Entity
class Accord(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val koreanName: String
) : BaseTimeEntity()