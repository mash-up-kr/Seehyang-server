package mashup.spring.seehyang.controller.api.dto.user

import mashup.spring.seehyang.domain.entity.community.OAuthType

data class SignUpRequest(
    val email : String,
    val oAuthType: OAuthType,
)

data class SignUpResponse (
    val token: String,
)
