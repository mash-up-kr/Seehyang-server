package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.user.*
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.service.UserService
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@ApiV1
class UserApiController(
    private val userService: UserService,
) {
    @Authenticated
    @GetMapping("/user")
    fun getUser(
        req: HttpServletRequest
    ): SeehyangResponse<UserDto> =
        SeehyangResponse(UserDto.from(userService.getUser(req)))

    @PostMapping("/user")
    fun signUpUser(
        @RequestBody body : SignUpRequest,
    ): SeehyangResponse<SignUpResponse> =
        SeehyangResponse(userService.signUpUser(body))

    @Authenticated
    @PutMapping("/user")
    fun registerUserDetailInfo(
        req: HttpServletRequest,
        @RequestBody body : RegisterUserDetailRequest,
    ): SeehyangResponse<RegisterUserDetailResponse> =
        SeehyangResponse(
            userService.registerUserDetail(
                req.getAttribute("userId").toString().toLong(), body))

    @GetMapping("/user/{nickname}")
    fun validDuplicateNickname(
        @PathVariable nickname: String,
    ): SeehyangResponse<DuplicateNicknameResponse> =
        SeehyangResponse(userService.isDuplicateNickname(nickname))

}