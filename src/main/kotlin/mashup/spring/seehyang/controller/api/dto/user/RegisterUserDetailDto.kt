package mashup.spring.seehyang.controller.api.dto.user

import mashup.spring.seehyang.domain.entity.perfume.Gender

data class RegisterUserDetailRequest(
    val gender: Gender,
    val age: Int,
    val nickname: String,
)

data class RegisterUserDetailResponse(
    val user: UserDto,
)