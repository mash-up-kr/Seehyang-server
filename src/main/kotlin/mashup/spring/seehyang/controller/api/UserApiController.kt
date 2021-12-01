package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.user.*
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
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
        @ApiIgnore userDto: UserDto?,
    ): SeehyangResponse<UserDto> {

        if(userDto ==null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val user = userService.getUser(userDto)

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
        @ApiIgnore userDto: UserDto?,
        @RequestParam(value = "imageId") imageId: Long
    ): SeehyangResponse<Pair<String, Long>> {

        if(userDto ==null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }
        userService.changeProfileImage(userDto, imageId)

        return SeehyangResponse(Pair("imageId", imageId))
    }

    @PutMapping("/user")
    fun registerUserDetailInfo(
        @ApiIgnore userDto: UserDto?,
        @RequestBody registerUserDetailRequest: RegisterUserDetailRequest,
    ): SeehyangResponse<RegisterUserDetailResponse> {

        if(userDto ==null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        return SeehyangResponse(userService.registerUserDetail(userDto, registerUserDetailRequest))
    }

    @DeleteMapping("/user")
    fun withdrawUser(
        @ApiIgnore userDto: UserDto?
    ): SeehyangResponse<Pair<String, Long>> {

        if(userDto ==null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }
        val withdrawnUserId = userService.withdrawUser(userDto)

        return SeehyangResponse(Pair("id", withdrawnUserId))
    }

    //TODO : MyPage 내 시향지(개수), 좋아요한 시향지(개수) 추가하기


}