package mashup.spring.seehyang.controller.api.dto.user

import mashup.spring.seehyang.domain.entity.user.OAuthType


data class SignUpRequest(
    val idToken: String,
    val oAuthType: OAuthType,
)

data class SignUpResponse (
    val token: String
)
