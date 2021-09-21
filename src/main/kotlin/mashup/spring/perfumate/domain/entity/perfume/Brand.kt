package mashup.spring.perfumate.domain.entity.perfume

import javax.persistence.*


@Entity
class Brand(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val koreanName: String,
    @OneToMany(mappedBy = "brand")
    val perfumes: List<Perfume> = ArrayList()
)