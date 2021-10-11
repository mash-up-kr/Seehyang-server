package mashup.spring.seehyang.controller.api.dto.user

data class SignInRequest(
    val email: String,
)

data class SignInResponse(
    val token: String,
)