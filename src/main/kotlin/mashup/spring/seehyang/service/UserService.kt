package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.user.*
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.user.OAuthType
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.util.UriGenerator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userJwtService: JwtService<Long>,
    private val oAuthService: OAuthService,
) {

    @Transactional
    fun signUpUser(req: SignUpRequest): SignUpResponse {
        val verifiedInfo = when(req.oAuthType) {
            OAuthType.GOOGLE -> oAuthService.verifyGoogle(req.idToken)
            else -> throw BadRequestException(SeehyangStatus.NOT_EXIST_OAUTH_TYPE)
        }

        if(userRepository.findByEmail(verifiedInfo.email).isPresent)
            throw BadRequestException(SeehyangStatus.ALREADY_EXIST_USER)

        val user = userRepository.save(
            User(email = verifiedInfo.email, oAuthType = req.oAuthType))

        return SignUpResponse(userJwtService.encode(user.id!!))
    }

    @Transactional
    fun registerUserDetail(id: Long,
                           req: RegisterUserDetailRequest
    ): RegisterUserDetailResponse {
        val user: User = userRepository.findById(id)
            .orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_USER) }
        user.age = req.age
        user.gender = req.gender
        user.nickname = req.nickname
        return RegisterUserDetailResponse(UserDto.from(user))
    }

    @Transactional(readOnly = true)
    fun isDuplicateNickname(nickname: String
    ): DuplicateNicknameResponse {
        return DuplicateNicknameResponse(
            isDuplicated = userRepository.findByNickname(nickname).isPresent)
    }

    @Transactional
    fun getUser(id: Long?): User {
        if(id == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }
        return userRepository.findById(id)
            .orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_USER) }
    }

    @Transactional(readOnly = true)
    fun signInUser(req: SignInRequest): SignInResponse {
        val user: User =
            userRepository.findByEmail(req.email)
                .orElseThrow{ NotFoundException(SeehyangStatus.NOT_FOUND_USER) }

        return SignInResponse(token = userJwtService.encode(user.id!!))
    }
}