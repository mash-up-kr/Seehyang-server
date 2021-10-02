package mashup.spring.seehyang.domain.entity.community

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.user.User
import javax.persistence.*

@Entity
class Story(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,


    /**
     * ========== One to One =========
     */

    @OneToOne
    @JoinColumn(name = "image_id")
    val image: Image,

    /**
     * ========== One to Many ==========
     * Like, Comment, Tagstory
     */

    /**
     * story는 여러개의 Like를 가질 수 있다.
     */
    @OneToMany(mappedBy = "story")
    val likes : MutableList<Like> = mutableListOf(),

    /**
     * story는 여러개의 Comments를 가질 수 있다.
     */
    @OneToMany(mappedBy = "story")
    val comments : MutableList<Comment> = mutableListOf(),

    /**
     * story는 여러개의 Tag를 가질 수 있다.
     */
    @OneToMany(mappedBy = "story")
    val storyTags: MutableList<StoryTag> = mutableListOf(),



    /**
     * ========= Many to One ==========
     * Perfume, User
     */

    /**
     * 어떤 Perfume 에 관련된 포스트인지 연결한다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    val perfume : Perfume,

    /**
     * 어떤 User 가 쓴 포스트인지 연결한다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User

) : BaseTimeEntity()