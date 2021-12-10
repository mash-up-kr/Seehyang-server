package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.user.*
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.service.UserService
import mashup.spring.seehyang.service.auth.UserId
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class UserApiController(
    private val userService: UserService,
) {
    @GetMapping("/user")
    fun getUser(
        @ApiIgnore userId: UserId?,
    ): SeehyangResponse<UserDto> {

        if(userId ==null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val user = userService.getUser(userId)

        return SeehyangResponse(user)
    }

    @GetMapping("/user/{nickname}")
    fun validDuplicateNickname(
        @PathVariable nickname: String,
    ): SeehyangResponse<DuplicateNicknameResponse> =
        SeehyangResponse(userService.isDuplicateNickname(nickname))

    @PostMapping("/user")
    fun signUpUser(
        @RequestBody body: SignUpRequest,
    ): SeehyangResponse<SignUpResponse> =
        SeehyangResponse(userService.signUpUser(body))

    @PostMapping("/user/profile")
    fun changeUserProfileImage(
        @ApiIgnore userId: UserId?,
        @RequestBody profileUpdateDto: ProfileUpdateDto
    ): SeehyangResponse<UserDto> {

        if(userId ==null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }
        val changedUser = userService.changeProfileImage(userId, profileUpdateDto)

        return SeehyangResponse(UserDto(changedUser))
    }

    @PutMapping("/user")
    fun registerUserDetailInfo(
        @ApiIgnore userId: UserId?,
        @RequestBody registerUserDetailRequest: RegisterUserDetailRequest,
    ): SeehyangResponse<UserDto> {

        if(userId ==null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        return SeehyangResponse(userService.registerUserDetail(userId, registerUserDetailRequest))
    }

    @DeleteMapping("/user")
    fun withdrawUser(
        @ApiIgnore userId: UserId?,
    ): SeehyangResponse<Pair<String, Long>> {

        if(userId ==null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }
        val withdrawnUserId = userService.withdrawUser(userId)

        return SeehyangResponse(Pair("id", withdrawnUserId))
    }

    //TODO : MyPage 내 시향지(개수), 좋아요한 시향지(개수) 추가하기


}