package mashup.spring.seehyang.domain.entity.perfume

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import javax.persistence.*

@Entity
class Brand(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val koreanName: String,
    @OneToMany(mappedBy = "brand")
    val perfumes : MutableList<Perfume> = mutableListOf()
) : BaseTimeEntity()