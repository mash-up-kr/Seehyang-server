package mashup.spring.seehyang.domain.entity.perfume

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    val brand: Brand,
    @OneToMany(mappedBy = "perfume")
    val accords: MutableList<PerfumeAccord> = mutableListOf(),
    @OneToMany(mappedBy = "perfume")
    val notes: MutableList<PerfumeNote> = mutableListOf(),
) : BaseTimeEntity()