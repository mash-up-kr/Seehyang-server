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

        val userId = validateDto(userDto)

        return userRepository.findById(userId).orElseThrow {NOT_FOUND_USER_EXCEPTION}
    }

    fun getUserByEmail(email: String):User {
        val validatedEmail = validateEmailFormat(email)
        return userRepository.findByEmail(validatedEmail).orElseThrow { NOT_FOUND_USER_EXCEPTION }
    }

    fun getUserByNickname(nickname: String): User{
        return userRepository.findByNickname(nickname).orElseThrow{NOT_FOUND_USER_EXCEPTION}
    }

    fun existsByNickname(nickname: String): Boolean {
        return userRepository.existsByNickname(nickname)
    }

    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
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

    private fun validateDto(userDto: UserDto) : Long{
        if(userDto.id == null)
            throw EMPTY_USER_IS_NOT_ALLOWED
        return userDto.id
    }

    private fun validateEmailFormat(email: String) :String{
        if(isValidEmailFormat(email)){
            throw INVALID_REQUESTED_EMAIL
        }
        return email
    }
}