package mashup.spring.perfumate.domain.entity.community

import mashup.spring.perfumate.domain.entity.BaseTimeEntity
import javax.persistence.*

@Entity
class Tag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val contents : String,

    @OneToMany(mappedBy = "tag")
    val tagPosts: MutableList<TagPost> = mutableListOf()

):BaseTimeEntity()