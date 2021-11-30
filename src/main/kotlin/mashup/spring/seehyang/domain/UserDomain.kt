package mashup.spring.seehyang.domain

import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.enums.Domain
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.exception.InternalServerException
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.util.isValidEmailFormat


@Domain
class UserDomain(
    private val userRepository: UserRepository
) {
    /**
     * Exceptions
     */
    private val NOT_FOUND_USER_EXCEPTION = NotFoundException(SeehyangStatus.NOT_FOUND_USER)
    private val INVALID_REQUESTED_EMAIL  = BadRequestException(SeehyangStatus.INVALID_EMAIL)
    private val MERGE_NOT_ALLOWED_EXCEPTION = InternalServerException(SeehyangStatus.INTERNAL_SERVER_ERROR)
    private val EMPTY_USER_IS_NOT_ALLOWED = UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)

    /**
     * Role and Responsibility (Public methods)
     */
    fun getUser(userDto: UserDto): User {

        return if(isNotEmptyUser(userDto)){
            userRepository.findById(userDto.id!!).orElseThrow {NOT_FOUND_USER_EXCEPTION}
        }else{
            User.empty()
        }
    }

    private fun isNotEmptyUser(userDto: UserDto): Boolean {
        return User.isLogin(userDto = userDto)
    }

    fun getUserByEmail(email: String):User {

        val validatedEmail = validateEmailFormat(email)

        val foundUser = userRepository.findByEmail(validatedEmail)?: throw NOT_FOUND_USER_EXCEPTION
        validateActiveUser(foundUser)

        return foundUser
    }

    fun getUserByNickname(nickname: String): User{

        val foundUser = userRepository.findByNickname(nickname)?: throw NOT_FOUND_USER_EXCEPTION

        validateActiveUser(foundUser)

        return foundUser
    }

    fun existsByNickname(nickname: String): Boolean {

        val foundUser = userRepository.findByNickname(nickname)

        return isExistAndActive(foundUser)
    }



    fun existsByEmail(email: String): Boolean {
        val foundUser =  userRepository.findByEmail(email)

        return isExistAndActive(foundUser)
    }

    fun saveUser(user: User): User{

        if(user.id != null){
            //logger.error("Merge Entity is not allowed")
            throw MERGE_NOT_ALLOWED_EXCEPTION
        }
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
        if(isActive.not()) throw NOT_FOUND_USER_EXCEPTION
    }




    private fun isActiveUser(foundUser: User): Boolean {
        return foundUser.isActivated
    }

    private fun validateEmailFormat(email: String) :String{
        if(isValidEmailFormat(email)){
            throw INVALID_REQUESTED_EMAIL
        }
        return email
    }
}