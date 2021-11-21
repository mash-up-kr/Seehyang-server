package mashup.spring.seehyang.domain.entity.user

import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.Comment
import mashup.spring.seehyang.domain.entity.community.StoryLike
import mashup.spring.seehyang.domain.entity.community.Story
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

        var email: String? = null,

        @Enumerated(EnumType.STRING)
    var oAuthType: OAuthType? = null,

    /**
     * ========== One to Many ==========
     * Post, Like, Comment
     */

        /**
     * User는 여러 개의 story를 쓸 수 있다.
     */
    @OneToMany(mappedBy = "user")
    val stories: MutableList<Story> = mutableListOf(),

        /**
     * User는 여러개의 like를 가질 수 있다.
     */
    @OneToMany(mappedBy = "user")
    val storyLikes: MutableSet<StoryLike> = mutableSetOf(),

        /**
     * User는 여러 개의 Comment를 가질 수 있다.
     */
    @OneToMany(mappedBy = "user")
    val comments: MutableList<Comment> = mutableListOf(),


        /**
     * ============== One to One =============
     */
    @OneToOne
    @JoinColumn(name = "image_id")
    val profileImage: Image? = null

) : BaseTimeEntity() {

    companion object {
        fun empty(): User =
            User(
                email = "",
                oAuthType = OAuthType.UNKNOWN
            )
    }

    fun isLogin(): Boolean =
        this.email.isNullOrBlank().not() || this.oAuthType != OAuthType.UNKNOWN
}