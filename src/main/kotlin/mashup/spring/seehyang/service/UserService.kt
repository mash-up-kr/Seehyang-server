package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.user.*
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.repository.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.servlet.http.HttpServletRequest
import kotlin.RuntimeException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userJwtService: JwtService<UserDto>,
) {

    @Transactional
    fun signUpUser(req: SignUpRequest): SignUpResponse {
        val user: User = userRepository.findByEmail(req.email)
            ?: userRepository.save(User(email = req.email, oAuthType = req.oAuthType))
        return SignUpResponse(userJwtService.encode(UserDto.from(user)))
    }

    @Transactional
    fun registerUserDetail(id: Long,
                           req: RegisterUserDetailRequest
    ): RegisterUserDetailResponse {
        val user: User = userRepository.findById(id)
            .orElseThrow { RuntimeException("Not Found User..") }
        user.age = req.age
        user.gender = req.gender
        user.nickname = req.nickname
        return RegisterUserDetailResponse(UserDto.from(user))
    }

    @Transactional(readOnly = true)
    fun isDuplicateNickname(nickname: String
    ): DuplicateNicknameResponse {
        return DuplicateNicknameResponse(
            isDuplicated = userRepository.findByNickname(nickname) != null
        )
    }

    @Transactional(readOnly = true)
    fun getUser(request: HttpServletRequest): User {
        return userRepository
            .findById(
                request.getAttribute("userId").toString().toLong()
            )
            .orElseThrow { RuntimeException("Not found user...") }
    }

    @Transactional(readOnly = true)
    fun signInUser(req: SignInRequest): SignInResponse {
        val user: User = userRepository.findByEmail(req.email)
            ?: throw RuntimeException("Not found user...")
        return SignInResponse(token = userJwtService.encode(UserDto.from(user)))
    }
}