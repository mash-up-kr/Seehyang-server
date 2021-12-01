package mashup.spring.seehyang.service.auth

import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.exception.InternalServerException
import mashup.spring.seehyang.repository.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class UserDetailsService (
    val userRepository: UserRepository
){

    fun getUserDtoByUserId(userId: Long): UserId?{

        val user = userRepository.findById(userId)

        return if(user.isPresent){
            UserId(user.get().id?:throw InternalServerException(SeehyangStatus.INVALID_USER_ENTITY))
        }else{
            null
        }

    }
}