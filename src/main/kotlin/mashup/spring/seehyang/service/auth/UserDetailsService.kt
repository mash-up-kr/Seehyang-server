package mashup.spring.seehyang.service.auth

import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.repository.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class UserDetailsService (
    val userRepository: UserRepository
){

    fun getUserDtoByUserId(userId: Long): UserDto?{

        val user = userRepository.findById(userId)

        return if(user.isPresent){
            UserDto(user.get())
        }else{
            null
        }

    }
}