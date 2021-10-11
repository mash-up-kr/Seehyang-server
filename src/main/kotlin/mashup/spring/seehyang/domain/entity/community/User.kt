package mashup.spring.seehyang.domain.entity.community

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import mashup.spring.seehyang.domain.entity.perfume.Gender
import javax.persistence.*


@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    var gender: Gender? = null,

    var age: Short? = null,

    var nickname: String? = null,

    val email: String,

    @Enumerated(EnumType.STRING)
    val oAuthType: OAuthType,

    /**
     * ========== One to Many ==========
     * Post, Like, Comment
     */

    /**
     * User는 여러 개의 Post를 쓸 수 있다.
     */
    @OneToMany(mappedBy = "user")
    val posts: MutableList<Post> = mutableListOf(),

    /**
     * User는 여러개의 like를 가질 수 있다.
     */
    @OneToMany(mappedBy = "user")
    val likes: MutableList<Like> = mutableListOf(),

    /**
     * User는 여러 개의 Comment를 가질 수 있다.
     */
    @OneToMany(mappedBy = "user")
    val comments: MutableList<Comment> = mutableListOf()


) : BaseTimeEntity()