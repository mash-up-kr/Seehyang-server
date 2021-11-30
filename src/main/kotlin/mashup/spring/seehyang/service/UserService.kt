package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.user.*
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.user.OAuthType
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.domain.UserDomain
import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.repository.ImageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userDomain: UserDomain,
    private val imageRepository: ImageRepository,
    private val userJwtService: JwtService<Long>,
    private val oAuthService: OAuthService,
) {

    @Transactional
    fun signUpUser(req: SignUpRequest): SignUpResponse {

        val verifiedInfo = verifyOAuthToken(req)
        validateDuplicatedEmail(verifiedInfo)
        val user = userDomain.saveUser(User(email = verifiedInfo.email, oAuthType = req.oAuthType))

        return SignUpResponse(userJwtService.encode(user.id!!))
    }


    @Transactional
    fun registerUserDetail(
        userDto: UserDto,
        req: RegisterUserDetailRequest
    ): RegisterUserDetailResponse {

        val user: User = userDomain.getUser(userDto)

        user.addUserInfo(age = req.age,
                         gender = req.gender,
                         nickname = req.nickname)

        return RegisterUserDetailResponse(UserDto(user))
    }

    @Transactional
    fun changeProfileImage(
        userDto: UserDto,
        imageId: Long
    ){

        val user = userDomain.getUser(userDto = userDto)
        val newImage = imageRepository.findById(imageId).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_IMAGE) }
        val oldImage = user.profileImage

        if(oldImage != null){
            imageRepository.deleteById(oldImage.id!!)
        }

        user.replaceProfileImage(newImage)
    }

    @Transactional(readOnly = true)
    fun isDuplicateNickname(
        nickname: String
    ): DuplicateNicknameResponse {

        return DuplicateNicknameResponse(userDomain.existsByNickname(nickname))
    }


    @Transactional(readOnly = true)
    fun getUser(userDto: UserDto): UserDto {

        return UserDto(userDomain.getUser(userDto))
    }


    @Transactional(readOnly = true)
    fun signInUser(req: SignInRequest): SignInResponse {

        val userEmail = req.email
        val user: User = userDomain.getUserByEmail(userEmail)

        return SignInResponse(token = userJwtService.encode(user.id!!))
    }

    @Transactional
    fun withdrawUser(userDto: UserDto): Long{

        val user= userDomain.getUser(userDto)
        user.disableUser()

        return user.id!!
    }

    /**
     * ============= Private Methods ===============
     */

    private fun verifyOAuthToken(req: SignUpRequest) = when (req.oAuthType) {
        OAuthType.GOOGLE -> oAuthService.verifyGoogle(req.idToken)
        else -> throw BadRequestException(SeehyangStatus.NOT_EXIST_OAUTH_TYPE)
    }

    private fun validateDuplicatedEmail(verifiedInfo: OAuthResponse) {
        val userEmail = verifiedInfo.email
        val isDuplicated = userDomain.existsByEmail(userEmail)

        if (isDuplicated){
            throw BadRequestException(SeehyangStatus.ALREADY_EXIST_USER)
        }
    }

}