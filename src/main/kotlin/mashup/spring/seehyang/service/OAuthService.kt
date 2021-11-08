package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.user.OAuthResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.util.UriGenerator
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class OAuthService(
    private val restTemplateService: RestTemplateService,
) {
    companion object {
        const val VERIFY_GOOGLE_AUTH_URL = "https://oauth2.googleapis.com/tokeninfo"
    }

    fun verifyGoogle(idToken: String) : OAuthResponse {
        val queryStrings = HashMap<String, String>()
        queryStrings["idToken"] = idToken
        try {
            return restTemplateService.get(
                UriGenerator.create(VERIFY_GOOGLE_AUTH_URL, queryStrings),
                HttpHeaders.EMPTY,
                OAuthResponse::class.java).body ?: throw RuntimeException()
        }catch (e: Exception) {
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_INVALID_TOKEN)
        }
    }
}