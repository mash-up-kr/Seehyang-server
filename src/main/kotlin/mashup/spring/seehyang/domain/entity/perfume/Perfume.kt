package mashup.spring.seehyang.domain.entity.perfume

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.community.StoryLike
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
    var likeCount: Int = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    val brand: Brand,
    @OneToMany(mappedBy = "perfume")
    val accords: MutableList<PerfumeAccord> = mutableListOf(),
    @OneToMany(mappedBy = "perfume")
    val notes: MutableList<PerfumeNote> = mutableListOf(),
    @OneToMany(mappedBy = "perfume")
    val stories: MutableList<Story> = mutableListOf(),
    @OneToMany(mappedBy = "perfume")
    val perfumeLikes : MutableList<PerfumeLike> = mutableListOf(),
) : BaseTimeEntity() {
    fun like() {
        this.likeCount ++
    }

    fun cancleLike() {
        this.likeCount --
    }
}