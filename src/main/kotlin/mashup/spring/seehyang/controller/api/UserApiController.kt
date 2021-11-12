package mashup.spring.seehyang.controller.api

import io.swagger.annotations.ApiParam
import mashup.spring.seehyang.controller.api.dto.user.*
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.service.UserService
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class UserApiController(
    private val userService: UserService,
) {
    @GetMapping("/user")
    fun getUser(
        @ApiIgnore user: User,
    ): SeehyangResponse<UserDto> {
        if(user.isLogin().not())
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        //TODO: Dto 생성 서비스로 옮기기
        return SeehyangResponse(UserDto.from(user))
    }

    @GetMapping("/user/{nickname}")
    fun validDuplicateNickname(
        @PathVariable nickname: String,
    ): SeehyangResponse<DuplicateNicknameResponse> =
        SeehyangResponse(userService.isDuplicateNickname(nickname))

    @PostMapping("/user")
    fun signUpUser(
        @RequestBody body : SignUpRequest,
    ): SeehyangResponse<SignUpResponse> =
        SeehyangResponse(userService.signUpUser(body))

    @PutMapping("/user")
    fun registerUserDetailInfo(
        @ApiIgnore user: User,
        @RequestBody body : RegisterUserDetailRequest,
    ): SeehyangResponse<RegisterUserDetailResponse> {
        if(user.isLogin().not())
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        return SeehyangResponse(userService.registerUserDetail(user.id!!, body))
    }



}