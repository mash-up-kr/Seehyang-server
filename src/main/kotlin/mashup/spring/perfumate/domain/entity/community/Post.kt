package mashup.spring.perfumate.domain.entity.community

import mashup.spring.perfumate.domain.entity.BaseTimeEntity
import mashup.spring.perfumate.domain.entity.perfume.Perfume
import mashup.spring.perfumate.domain.entity.community.User
import javax.persistence.*

@Entity
class Post(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val contents: String,

    /**
     * ========== One to Many ==========
     * Like, Comment, TagPost
     */

    /**
     * Post는 여러개의 Like를 가질 수 있다.
     */
    @OneToMany(mappedBy = "post")
    val likes : MutableList<Like> = mutableListOf(),

    /**
     * Post는 여러개의 Comments를 가질 수 있다.
     */
//    @OneToMany(mappedBy = "post")
//    val comments : MutableList<Comment> = mutableListOf(),

    /**
     * Post는 여러개의 Tag를 가질 수 있다.
     */
    @OneToMany(mappedBy = "post")
    val posts: MutableList<TagPost> = mutableListOf(),



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