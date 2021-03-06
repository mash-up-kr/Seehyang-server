package mashup.spring.seehyang.domain

import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.user.OAuthType
import mashup.spring.seehyang.domain.enums.Domain
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.service.auth.UserId


@Domain
class UserDomain(
    private val userRepository: UserRepository
) {

    /**
     * =========== Exceptions ==================
     */
    private val NOT_FOUND_USER_EXCEPTION = NotFoundException(SeehyangStatus.NOT_FOUND_USER)


    /**
     * =========== Responsibilities =============
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


        val foundUser = userRepository.findByEmail(email)
        if(foundUser.isEmpty()) throw NOT_FOUND_USER_EXCEPTION

        validateActiveUser(foundUser.last())

        return foundUser.last()
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


    fun signUpUser(email: String, oAuthType: OAuthType): User {

        validateDuplicatedEmailWithInActiveUser(email)


        val user = User(email = email, oAuthType = oAuthType)

        return userRepository.save(user)
    }

    /**
     * Private Methods
     */




    private fun validateActiveUser(foundUser: User) {

        val isActive = isActiveUser(foundUser)

        if (isActive.not()) throw NOT_FOUND_USER_EXCEPTION
    }


    private fun isActiveUser(foundUser: User): Boolean {

        return foundUser.isActivated
    }


    private fun validateDuplicatedEmailWithInActiveUser(email: String) {

        val isDuplicated = existsByEmail(email)

        if (isDuplicated) {
            throw BadRequestException(SeehyangStatus.ALREADY_EXIST_USER)
        }
    }

    private fun existsByEmail(email: String): Boolean {

        val foundUser = userRepository.findByEmail(email)
        if(foundUser.isEmpty()) return false

        return isExistAndActive(foundUser.last())
    }
    private fun isExistAndActive(foundUser: User?): Boolean {

        return foundUser != null && foundUser.isActivated
    }
}