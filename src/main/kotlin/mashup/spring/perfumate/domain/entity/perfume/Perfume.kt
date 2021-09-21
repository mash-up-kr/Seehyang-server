package mashup.spring.perfumate.domain.entity.perfume

import javax.persistence.*

@Entity
class Perfume(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val koreanName: String,
    @Enumerated(EnumType.STRING)
    val gender: Gender,
    val thumbnailUrl: String,
    @ManyToOne
    @JoinColumn(name = "brand_id")
    val brand: Brand
)