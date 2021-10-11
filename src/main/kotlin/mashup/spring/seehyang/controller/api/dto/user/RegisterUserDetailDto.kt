package mashup.spring.seehyang.controller.api.dto.user

import mashup.spring.seehyang.domain.entity.community.User
import mashup.spring.seehyang.domain.entity.perfume.Gender

data class RegisterUserDetailRequest(
    val gender: Gender,
    val age: Short,
    val nickname: String,
)

data class RegisterUserDetailResponse(
    val user: User,
)