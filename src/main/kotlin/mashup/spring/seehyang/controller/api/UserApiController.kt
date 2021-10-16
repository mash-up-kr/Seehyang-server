package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.config.resolver.Logined
import mashup.spring.seehyang.controller.api.dto.user.*
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.service.UserService
import org.springframework.web.bind.annotation.*

@ApiV1
class UserApiController(
    private val userService: UserService,
) {
    @GetMapping("/user")
    fun getUser(
        @Logined user: User,
    ): SeehyangResponse<UserDto> {
        if(user.isLogin().not()) throw RuntimeException("Not Authorization user..")
        return SeehyangResponse(UserDto.from(user))
    }

    @PostMapping("/user")
    fun signUpUser(
        @RequestBody body : SignUpRequest,
    ): SeehyangResponse<SignUpResponse> =
        SeehyangResponse(userService.signUpUser(body))

    @PutMapping("/user")
    fun registerUserDetailInfo(
        @Logined user: User,
        @RequestBody body : RegisterUserDetailRequest,
    ): SeehyangResponse<RegisterUserDetailResponse> {
        if(user.isLogin().not()) throw RuntimeException("Not Authorization user..")
        return SeehyangResponse(userService.registerUserDetail(user.id!!, body))
    }

    @GetMapping("/user/{nickname}")
    fun validDuplicateNickname(
        @PathVariable nickname: String,
    ): SeehyangResponse<DuplicateNicknameResponse> =
        SeehyangResponse(userService.isDuplicateNickname(nickname))

}