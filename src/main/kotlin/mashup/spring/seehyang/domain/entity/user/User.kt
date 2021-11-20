package mashup.spring.seehyang.domain.entity.user

import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.BaseTimeEntity
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.Comment
import mashup.spring.seehyang.domain.entity.community.StoryLike
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.perfume.Gender
import mashup.spring.seehyang.exception.BadRequestException
import java.util.regex.Pattern
import javax.persistence.*


@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    email: String,

    oAuthType: OAuthType,


) : BaseTimeEntity() {


    @Enumerated(EnumType.STRING)
    var gender: Gender? = null
        protected set

    var age: Short? = null
        protected set

    var nickname: String? = null
        protected set

    var email: String? = email
        protected set

    @Enumerated(EnumType.STRING)
    var oAuthType: OAuthType? = oAuthType
        protected set
    /**
     * =============== PK ===================
     */

    @OneToOne
    @JoinColumn(name = "image_id")
    var profileImage: Image? = null
        protected set

    @OneToMany(mappedBy = "user")
    val stories: MutableList<Story> = mutableListOf()

    @OneToMany(mappedBy = "user")
    val storyLikes: MutableList<StoryLike> = mutableListOf()


    @OneToMany(mappedBy = "user")
    val comments: MutableList<Comment> = mutableListOf()


    companion object {
        fun empty(): User =
            User(
                email = "",
                oAuthType = OAuthType.UNKNOWN
            )
    }



    fun isLogin(): Boolean =
        this.email.isNullOrBlank().not() || this.oAuthType != OAuthType.UNKNOWN

    fun changeAge(age: Int){
        validationAge(age)
        this.age = age.toShort()
    }

    fun changeGender(gender: Gender){
        this.gender = gender
    }

    fun changeNickname(nickname: String){
        validationNickname(nickname)
        this.nickname = nickname
    }



    //TODO 정규식 왜이래
    private fun validationNickname(nickname: String) {
        if(Pattern.matches("", nickname).not()){
            throw BadRequestException(SeehyangStatus.INVALID_NICKNAME)
        }
    }

    private fun validationAge(age: Int) {
        if(age < 0 || age > 100) throw BadRequestException(SeehyangStatus.INVALID_AGE)
    }
}