package mashup.spring.seehyang.domain.entity.user

import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.repository.user.UserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserTest @Autowired constructor(
    val userRepository: UserRepository
) {

    @Test
    fun changeGender() {
    }

    @DisplayName("Valid Nickname Test")
    @Test
    fun changeValidNickname(){
        val testUser = User(email = "testEmail",oAuthType = OAuthType.GOOGLE)
        userRepository.save(testUser)
        val user = userRepository.findByEmail("testEmail").get()

        var validNick = "ValidNick12"
        user.changeNickname(validNick)
        assertEquals(user.nickname!!, validNick)

        validNick = "ValidNick"
        user.changeNickname(validNick)
        assertEquals(user.nickname!!, validNick)

        validNick = "가힣"
        user.changeNickname(validNick)
        assertEquals(user.nickname!!, validNick)

        validNick = "닉네임123abc"
        user.changeNickname(validNick)
        assertEquals(user.nickname!!, validNick)

        validNick = "닉네임123abc"
        user.changeNickname(validNick)
        assertEquals(user.nickname!!, validNick)


    }

    @DisplayName("Invalid Nickname Test")
    @Test
    fun changeInvalidNickname() {
        val testUser = User(email = "testEmail",oAuthType = OAuthType.GOOGLE)
        val savedUser = userRepository.save(testUser)

        val specialCharacter = "SpecialString!"
        val spaceNickname = "Nick Name"
        val noDapNickname = "!!! @@@ ####"
        val hanjaNickname = "日本語"

        val user = userRepository.findByEmail("testEmail").get()

        assertThrows(BadRequestException::class.java){
            user.changeNickname(specialCharacter)
        }
        assertThrows(BadRequestException::class.java){
            user.changeNickname(spaceNickname)
        }
        assertThrows(BadRequestException::class.java){
            user.changeNickname(noDapNickname)
        }
        assertThrows(BadRequestException::class.java){
            user.changeNickname(hanjaNickname)
        }

        assertThrows(BadRequestException::class.java){
            user.changeNickname("")
        }

        assertThrows(BadRequestException::class.java){
            user.changeNickname(" ")
        }

    }
}