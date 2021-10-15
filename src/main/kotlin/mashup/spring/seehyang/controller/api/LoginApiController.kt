package mashup.spring.seehyang.controller.api;

import mashup.spring.seehyang.controller.api.dto.user.SignInRequest
import mashup.spring.seehyang.controller.api.dto.user.SignInResponse
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@ApiV1
class LoginApiController(
    private val userService: UserService
) {

    @GetMapping("/login")
    fun login(req : SignInRequest,
    ): SeehyangResponse<SignInResponse> =
        SeehyangResponse(userService.signInUser(req))
}
