package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.user.*
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.user.OAuthType
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.domain.UserDomain
import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.exception.InternalServerException
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.service.auth.JwtService
import mashup.spring.seehyang.service.auth.OAuthService
import mashup.spring.seehyang.service.auth.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userDomain: UserDomain,
    private val imageRepository: ImageRepository,
    private val userJwtService: JwtService<Long>,
    private val oAuthService: OAuthService, // TODO 도메인으로 변경 필요
) {

    private val EMPTY_USER_IS_NOT_ALLOWED = UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)

    @Transactional
    fun signUpUser(req: SignUpRequest): SignUpResponse {

        val verifiedInfo = verifyOAuthToken(req)

        val user = userDomain.signUpUser(verifiedInfo.email, req.oAuthType)

        return SignUpResponse(userJwtService.encode(user.id!!))
    }


    @Transactional
    fun registerUserDetail(
        userId: UserId,
        req: RegisterUserDetailRequest
    ): UserDto {

        val user: User? = userDomain.getLoginUser(userId)

        val validUser = validateLoginUser(user)

        validUser.setUserInfo(
            age = req.age,
            gender = req.gender,
            nickname = req.nickname
        )

        return UserDto(validUser)


    }


    @Transactional
    fun changeProfileImage(
        userId: UserId,
        profileUpdateDto: ProfileUpdateDto
    ): User {

        val user = userDomain.getLoginUser(userId = userId)

        val validUser = validateLoginUser(user)

        val imageId = profileUpdateDto.imageId
        val nickname = profileUpdateDto.nickname

        if (imageId != null){
            val newImage = imageRepository.findById(imageId).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_IMAGE) }
            val oldImage = validUser.profileImage

            if (oldImage != null) {
                imageRepository.deleteById(oldImage.id ?: throw InternalServerException(SeehyangStatus.INVALID_IMAGE_ENTITY))
            }
            validUser.changeProfileImage(newImage)
        }

        if (nickname != null) {
            validUser.changeNickname(nickname)
        }

        return validUser
    }

    @Transactional(readOnly = true)
    fun isDuplicateNickname(
        nickname: String
    ): DuplicateNicknameResponse {

        return DuplicateNicknameResponse(userDomain.existsByNickname(nickname))
    }


    @Transactional(readOnly = true)
    fun getUser(userId: UserId): UserDto {

        val user = userDomain.getLoginUser(userId = userId)
        val validUser = validateLoginUser(user)

        return UserDto(validUser)
    }


    @Transactional(readOnly = true)
    fun signInUser(req: SignInRequest): SignInResponse {

        val userEmail = req.email
        val user: User = userDomain.getUserByEmail(userEmail)

        return SignInResponse(token = userJwtService.encode(user.id!!))
    }

    @Transactional
    fun withdrawUser(userId: UserId): Long {

        val user = userDomain.getLoginUser(userId)
        val validUser = validateLoginUser(user)

        val userId = validUser.id ?: throw InternalServerException(SeehyangStatus.INVALID_USER_ENTITY)

        validUser.inactivateUser()

        return userId
    }

    /**
     * ============= Private Methods ===============
     */

    private fun verifyOAuthToken(req: SignUpRequest) = when (req.oAuthType) {

        OAuthType.GOOGLE -> oAuthService.verifyGoogle(req.idToken)
        else -> throw BadRequestException(SeehyangStatus.NOT_EXIST_OAUTH_TYPE)
    }

    fun validateLoginUser(user: User?): User {

        if (user == null) {
            throw EMPTY_USER_IS_NOT_ALLOWED
        }
        return user
    }


}