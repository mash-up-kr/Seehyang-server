package mashup.spring.seehyang.domain.entity.community

import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.NotFoundException
import javax.persistence.*

@Entity
class Comment(
    id: Long? =null,
    contents: String,
    story: Story,
    user:User
) : BaseTimeEntity(){
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = id

    val contents: String = contents

    var numOfLike: Int = 0
        protected set

    var numOfDislike: Int = 0
        protected set

    var numOfReply: Int = 0
        protected set

    /**
     * Comment Self-Join
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    var parent : Comment? = null
        protected set

    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val children : MutableList<Comment> = mutableListOf()

    @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val commentLikes : MutableList<CommentLike> = mutableListOf()

    @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val commentDislike : MutableList<CommentDislike> = mutableListOf()


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    val story: Story = story

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User = user


    /**
     * =============== Public Methods ============
     */

    /**
     * === Reply Comment ===
     */


    fun addReplyComment(contents: String, user:User) : Comment{

        val comment = Comment(contents = contents, user = user,story = story)

        comment.setParentComment(this)
        children.add(comment)
        numOfReply = children.size

        return comment
    }

    fun deleteReplyComment(commentId: Long){
        val replyComment = children.firstOrNull { it.id == commentId }

        if(replyComment != null){
            children.remove(replyComment)
        }else{
            throw NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT)
        }
        numOfReply = children.size
    }



    /**
     * === Comment Like / Dislike ===
     */

    fun likeComment(user: User): Boolean{

        val likeComment: CommentLike? = getLikeComment(user)
        var isLiked = false

        if(likeComment != null){
            commentLikes.remove(likeComment)
        }else{
            commentLikes.add(CommentLike(user = user, comment = this))
            isLiked = true
        }

        numOfLike = commentLikes.size

        return isLiked
    }

    fun disLikeComment(user: User): Boolean{
        val disLikeComment: CommentDislike? = getDislikeComment(user)
        var isDislike = false

        if(disLikeComment != null){
            commentDislike.remove(disLikeComment)
        }else{
            commentDislike.add(CommentDislike(user = user, comment = this))
            isDislike = true
        }

        numOfLike = commentLikes.size

        return isDislike
    }

    /**
     * ================== Private Methods ===================
     */

    private fun getLikeComment(user: User):CommentLike?{
        return commentLikes.firstOrNull{it.user.id == user.id}
    }

    private fun getDislikeComment(user: User):CommentDislike?{
        return commentDislike.firstOrNull{it.user.id == user.id}
    }
    private fun setParentComment(comment: Comment){
        this.parent = comment
    }


}