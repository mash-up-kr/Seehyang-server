package mashup.spring.seehyang.domain.entity.community

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import mashup.spring.seehyang.domain.entity.user.User
import javax.persistence.*

@Entity
class CommentLike (
    id: Long? = null,
    user: User,
    comment: Comment
):BaseTimeEntity(){

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User = user

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    val comment : Comment = comment

}