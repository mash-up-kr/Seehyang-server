package mashup.spring.seehyang.domain.entity.community

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import javax.persistence.*

@Entity
class Tag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    val contents : String,


):BaseTimeEntity(){

    @OneToMany(mappedBy = "tag")
    private val storyTags: MutableList<StoryTag> = mutableListOf()

    fun addStoryTag(storyTag: StoryTag){
        this.storyTags.add(storyTag)
    }

    fun viewStoryTag(): List<StoryTag>{
        return ArrayList(storyTags)
    }
}