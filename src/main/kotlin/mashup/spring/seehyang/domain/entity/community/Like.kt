package mashup.spring.seehyang.domain.entity.community

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "like_table")
class Like(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /**
     *  ========== Many to One ==========
     *  User, Post
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post

) : BaseTimeEntity()
