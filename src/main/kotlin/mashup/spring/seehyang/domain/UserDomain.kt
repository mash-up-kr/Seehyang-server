package mashup.spring.seehyang.domain

import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.user.OAuthType
import mashup.spring.seehyang.domain.enums.Domain
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.exception.InternalServerException
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.service.auth.UserId
import mashup.spring.seehyang.util.isValidEmailFormat


@Domain
class UserDomain(
    private val userRepository: UserRepository
) {
    /**
     * Exceptions
     */
    private val NOT_FOUND_USER_EXCEPTION = NotFoundException(SeehyangStatus.NOT_FOUND_USER)

    /**
     * Role and Responsibility (Public methods)
     */
    fun getLoginUser(userId: UserId): User? {

        val user = userRepository.findById(userId.id).orElseThrow { NOT_FOUND_USER_EXCEPTION }
        val activeUser = isActiveUser(user)

        if (activeUser) {
            return user
        }

        return null
    }


    fun getUserByEmail(email: String): User {


        val foundUser = userRepository.findByEmail(email) ?: throw NOT_FOUND_USER_EXCEPTION

        validateActiveUser(foundUser)

        return foundUser
    }

    fun getUserByNickname(nickname: String): User {

        val foundUser = userRepository.findByNickname(nickname) ?: throw NOT_FOUND_USER_EXCEPTION

        validateActiveUser(foundUser)

        return foundUser
    }

    fun existsByNickname(nickname: String): Boolean {

        val foundUser = userRepository.findByNickname(nickname)

        return isExistAndActive(foundUser)
    }


    fun saveUser(email: String, oAuthType: OAuthType): User {

        validateDuplicatedEmail(email)

        val user = User(email = email, oAuthType = oAuthType)

        return userRepository.save(user)
    }

    /**
     * Private Methods
     */

    private fun isExistAndActive(foundUser: User?): Boolean {

        return foundUser != null && foundUser.isActivated
    }


    private fun validateActiveUser(foundUser: User) {

        val isActive = isActiveUser(foundUser)

        if (isActive.not()) throw NOT_FOUND_USER_EXCEPTION
    }


    private fun isActiveUser(foundUser: User): Boolean {

        return foundUser.isActivated
    }


    private fun validateDuplicatedEmail(email: String) {

        val isDuplicated = existsByEmail(email)

        if (isDuplicated) {
            throw BadRequestException(SeehyangStatus.ALREADY_EXIST_USER)
        }
    }

    fun existsByEmail(email: String): Boolean {

        val foundUser = userRepository.findByEmail(email)

        return isExistAndActive(foundUser)
    }
}