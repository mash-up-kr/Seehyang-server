package mashup.spring.seehyang.domain.entity.community

import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.NotFoundException
import javax.persistence.*

@Entity
class Story(
    id: Long? = null,
    isOnlyMe: Boolean,
    image: Image,
    perfume : Perfume,
    user : User
) : BaseTimeEntity(){

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = id


    //TODO: isOnlyMe 관련 로직 추가 필요
    var isOnlyMe: Boolean = isOnlyMe
        protected set

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    val image: Image = image

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    val perfume : Perfume = perfume

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User = user

    var likeCount: Int = 0
        protected set
    var commentCount: Int = 0
        protected set

    @OneToMany(mappedBy = "story", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val comments : MutableList<Comment> = mutableListOf()


    /**
     * ========== One to Many ==========
     * Like, Comment, Tagstory
     */

    @OneToMany(mappedBy = "story", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val storyLikes : MutableList<StoryLike> = mutableListOf()


    @OneToMany(mappedBy = "story", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val storyTags: MutableList<StoryTag> = mutableListOf()




    /**
     * ================ Public Methods =======================
     */

    fun viewComments() :List<Comment> = ArrayList(comments)


    fun viewStoryTags(): List<StoryTag> = ArrayList(storyTags)


    fun likeStory(user: User):Boolean{

        val userLike = findUserLike(user)
        if(userLike != null){
            cancelLike(userLike)
            return false
        }else{
            doLike(user)
            return true
        }

    }

    fun isUserLike(user: User) : Boolean {
        return storyLikes.any{ it.user.id == user.id!! }
    }


    fun addComment(contents: String, user: User) {
        val comment = Comment(contents = contents, user = user, story = this)
        comments.add(comment)
        this.commentCount = comments.size
    }

    fun deleteComment(commentId: Long, user:User): Boolean{
        val isDeleted = comments.removeIf { it.id == commentId && it.user.id == user.id!! }
        return isDeleted
    }

//    fun getReplyComments(parentId: Long): List<Comment>{
//        val parent = getComment(parentId)
//        return parent.children
//    }

    fun addReplyComment(commentId: Long, contents: String, user: User):Boolean {

        val comment = getComment(commentId)
        if(comment.parent != null){
            comment.addReplyComment(contents, user)
            return true
        }
        return false

    }

    fun deleteReplyComment(commentId: Long, user:User){
        val comment = getComment(commentId)

        comment.deleteReplyComment(commentId)
    }


    fun addStoryTag(storyTag: StoryTag) {
        storyTags.add(storyTag)
    }

    fun deleteStoryTag(storyTag: StoryTag): Boolean{
        val isRemoved = storyTags.removeIf { it.id == storyTag.id }
        return isRemoved
    }



    /**
     * ================== Private Methods ==================
     */

    private fun findUserLike(user: User) : StoryLike? {
        return storyLikes.find { it.user.id == user.id!! }
    }

    private fun doLike(user: User){
        storyLikes.add(StoryLike(user = user, story = this))
        likeCount = storyLikes.size
    }

    private fun cancelLike(storyLike: StoryLike){
         storyLikes.remove(storyLike)
         likeCount = storyLikes.size
    }

    private fun getComment(commentId : Long) : Comment{
        return comments.firstOrNull{it.id == commentId} ?: throw NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT)
    }



}