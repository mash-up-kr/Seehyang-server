package mashup.spring.seehyang.domain.entity.community

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import javax.persistence.*

@Entity
class Tag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    val contents : String,

    @OneToMany(mappedBy = "tag")
    val storyTags: MutableList<StoryTag> = mutableListOf()

):BaseTimeEntity()