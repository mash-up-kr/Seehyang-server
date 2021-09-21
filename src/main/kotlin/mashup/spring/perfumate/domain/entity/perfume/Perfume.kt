package mashup.spring.perfumate.domain.entity.perfume

import javax.persistence.*

@Entity
class Perfume(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val koreanName: String,
    @Enumerated(EnumType.STRING)
    val gender: Gender,
    val thumbnailUrl: String
)