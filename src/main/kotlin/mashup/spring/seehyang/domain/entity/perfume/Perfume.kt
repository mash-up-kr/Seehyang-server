package mashup.spring.seehyang.domain.entity.perfume

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import mashup.spring.seehyang.domain.entity.community.Story
import javax.persistence.*

@Entity
class Perfume(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var name: String,
    var koreanName: String,
    @Enumerated(EnumType.STRING)
    val type: PerfumeType,
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
    @OneToMany(mappedBy = "perfume")
    val stories: MutableList<Story> = mutableListOf(),
) : BaseTimeEntity()