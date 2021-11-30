package mashup.spring.seehyang.controller.api.dto.user

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.user.OAuthType
import mashup.spring.seehyang.domain.entity.perfume.Gender
import mashup.spring.seehyang.domain.entity.user.User

data class UserDto(
    @JsonIgnore
    val user:User,
    val id :Long? = user.id,
    val gender: Gender? = user.gender,
    val age:Short? = user.age,
    val nickname:String? = user.nickname,
    val email:String? = user.email,
    val oAuthType:OAuthType? = user.oAuthType,
    val profileImage:Image? = user.profileImage,
    val isActivated:Boolean = user.isActivated
) {

}