package mashup.spring.perfumate.domain.entity.community

import mashup.spring.perfumate.domain.entity.BaseTimeEntity
import mashup.spring.perfumate.domain.entity.perfume.Gender
import javax.persistence.*


@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    val gender: Gender,

    val age: Short,

    val nickname: String,

    val email: String,

    /**
     * ========== One to Many ==========
     * Post, Like, Comment
     */


    /**
     * User는 여러 개의 Post를 쓸 수 있다.
     */
    @OneToMany(mappedBy = "post")
    val posts: MutableList<Post> = mutableListOf(),

    /**
     * User는 여러개의 like를 가질 수 있다.
     */
    @OneToMany(mappedBy = "like")
    val likes: MutableList<Like> = mutableListOf(),

    /**
     * User는 여러 개의 Comment를 가질 수 있다.
     */
    @OneToMany(mappedBy = "comment")
    val comments: MutableList<Comment> = mutableListOf()


) : BaseTimeEntity()