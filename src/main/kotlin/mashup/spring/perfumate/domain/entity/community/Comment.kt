package mashup.spring.perfumate.domain.entity.community

import mashup.spring.perfumate.domain.entity.BaseTimeEntity
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
    @JoinColumn(name = "comment_id")
    val parent : Comment,

    @OneToMany(mappedBy = "comment")
    val children : MutableList<Comment> = mutableListOf(),

    /**
     * ======== Many to One ===========
     * Post
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post

) : BaseTimeEntity()