package mashup.spring.seehyang.controller.api.dto.user

import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.OAuthType
import mashup.spring.seehyang.domain.entity.perfume.Gender
import mashup.spring.seehyang.domain.entity.user.User

class UserDto(
    val id: Long,
    var gender: Gender? = null,
    var age: Short? = null,
    var nickname: String? = null,
    val email: String,
    val oAuthType: OAuthType,
    val profileImage: Image? = null
) {
    companion object {
        fun from(user: User): UserDto = UserDto(
          id = user.id!!,
          gender = user.gender,
          age = user.age,
          nickname = user.nickname,
          email = user.email,
          oAuthType = user.oAuthType,
          profileImage = user.profileImage
        )
    }
}