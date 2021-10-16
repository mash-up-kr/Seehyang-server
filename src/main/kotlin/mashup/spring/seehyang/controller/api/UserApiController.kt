package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.config.resolver.Logined
import mashup.spring.seehyang.controller.api.dto.user.*
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.service.UserService
import org.springframework.web.bind.annotation.*

@ApiV1
class UserApiController(
    private val userService: UserService,
) {
    @GetMapping("/user")
    fun getUser(
        @Logined userId: Long?,
    ): SeehyangResponse<UserDto> =
        SeehyangResponse(UserDto.from(userService.getUser(userId!!)))

    @PostMapping("/user")
    fun signUpUser(
        @RequestBody body : SignUpRequest,
    ): SeehyangResponse<SignUpResponse> =
        SeehyangResponse(userService.signUpUser(body))

    @PutMapping("/user")
    fun registerUserDetailInfo(
        @Logined userId: Long?,
        @RequestBody body : RegisterUserDetailRequest,
    ): SeehyangResponse<RegisterUserDetailResponse> =
        SeehyangResponse(userService.registerUserDetail(userId!!, body))

    @GetMapping("/user/{nickname}")
    fun validDuplicateNickname(
        @PathVariable nickname: String,
    ): SeehyangResponse<DuplicateNicknameResponse> =
        SeehyangResponse(userService.isDuplicateNickname(nickname))

}