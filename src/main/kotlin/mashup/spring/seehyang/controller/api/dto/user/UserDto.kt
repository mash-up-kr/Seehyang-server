package mashup.spring.seehyang.controller.api.dto.user

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.user.OAuthType
import mashup.spring.seehyang.domain.entity.perfume.Gender
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.InternalServerException

data class UserDto(
    @JsonIgnore
    val user:User,
    val id :Long = user.id?: throw InternalServerException(SeehyangStatus.INVALID_USER_ENTITY),
    val email:String = user.email?: throw InternalServerException(SeehyangStatus.INVALID_USER_ENTITY),
    val oAuthType:OAuthType = user.oAuthType?: throw InternalServerException(SeehyangStatus.INVALID_USER_ENTITY),
    val gender: Gender? = user.gender,
    val age:Short? = user.age,
    val nickname:String? = user.nickname,
    val profileImage:Image? = user.profileImage,
    val isActivated:Boolean = user.isActivated
) {

}