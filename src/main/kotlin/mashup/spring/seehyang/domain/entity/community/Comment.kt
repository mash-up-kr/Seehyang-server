package mashup.spring.seehyang.domain.entity.community

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import javax.persistence.*

@Entity
class Comment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val contents: String,

    val numOfLike: Int,

    val numOfDislike: Int,

    /**
     * Comment Self-Join
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    val parent : Comment,

    @OneToMany(mappedBy = "parent")
    val children : MutableList<Comment> = mutableListOf(),

    /**
     * ======== Many to One ===========
     * Post
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User

) : BaseTimeEntity()