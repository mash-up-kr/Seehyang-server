package mashup.spring.perfumate.domain.entity.perfume

import javax.persistence.*

@Entity
class Tag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String
)