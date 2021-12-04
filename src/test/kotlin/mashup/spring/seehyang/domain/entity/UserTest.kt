package mashup.spring.seehyang.domain.entity

import mashup.spring.seehyang.createTestUser
import mashup.spring.seehyang.domain.entity.perfume.Gender
import mashup.spring.seehyang.domain.entity.user.OAuthType
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.BadRequestException
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*


class UserTest {


    /**
     *  1. Age 변경 테스트
     *  Age: 1~100살
     */

    //성공 테스트
    @Test
    fun changeAge() {
        val testAge :Int = 25
        val user = createTestUser(false)
        user.changeAge(testAge)
        assertEquals(user.age, testAge.toShort())
    }

    //실패 테스트
    @Test
    fun changeAgeFail(){
        val user = createTestUser(false)

        assertThrows(BadRequestException::class.java){user.changeAge(101)}
        assertThrows(BadRequestException::class.java){user.changeAge(0)}
        assertThrows(BadRequestException::class.java){user.changeAge(-1)}
    }

    /**
     * Gender 변경 테스트
     */
    @Test
    fun changeGender(){
        val user = createTestUser(false)

        user.changeGender(Gender.M)

        assertEquals(user.gender, Gender.M)
    }


    /**
     * Email 테스트
     */

    @Test
    fun createUserValidEmail(){

        val validEmail = "test@test.com"
        assertDoesNotThrow(){ User(validEmail, OAuthType.GOOGLE) }
    }

    @Test
    fun createUserInvalidEmail(){

        val invalidEmail1 = ""
        val invalidEmail2 = " "
        val invalidEmail3 = "aaa"
        val invalidEmail4 = "aaa@"
        val invalidEmail5 = "aaa!"
        val invalidEmail6 = "aaa@nafa"
        val invalidEmail7 = "aaa@nafa."

        assertThrows(BadRequestException::class.java){ User(invalidEmail1, OAuthType.GOOGLE) }
        assertThrows(BadRequestException::class.java){ User(invalidEmail2, OAuthType.GOOGLE) }
        assertThrows(BadRequestException::class.java){ User(invalidEmail3, OAuthType.GOOGLE) }
        assertThrows(BadRequestException::class.java){ User(invalidEmail4, OAuthType.GOOGLE) }
        assertThrows(BadRequestException::class.java){ User(invalidEmail5, OAuthType.GOOGLE) }
        assertThrows(BadRequestException::class.java){ User(invalidEmail6, OAuthType.GOOGLE) }
        assertThrows(BadRequestException::class.java){ User(invalidEmail7, OAuthType.GOOGLE) }
    }


    /**
     * Nickname 테스트
     */
    @Test
    fun changeValidNickname(){
        val validNickname1 = "nickname123"
        val validNickname2 = "1234"
        val validNickname3 = "유효한닉네임"

        val user = createTestUser(false)

        user.changeNickname(validNickname1)
        assertEquals(user.nickname,validNickname1)

        user.changeNickname(validNickname2)
        assertEquals(user.nickname,validNickname2)

        user.changeNickname(validNickname3)
        assertEquals(user.nickname,validNickname3)

    }

    @Test
    fun changeInvalidNickname(){
        val tooLongNickname = "nickname123456789"
        val spaceNickname = "1 234"
        val emptyNickname = "    "
        val weiredNickname = "@@@!!!12"
        val hanjaNickname = "日本語"

        val user = createTestUser(false)

        assertThrows(BadRequestException::class.java){user.changeNickname(tooLongNickname)}
        assertThrows(BadRequestException::class.java){user.changeNickname(spaceNickname)}
        assertThrows(BadRequestException::class.java){user.changeNickname(emptyNickname)}
        assertThrows(BadRequestException::class.java){user.changeNickname(weiredNickname)}
        assertThrows(BadRequestException::class.java){user.changeNickname(hanjaNickname)}

    }

    @Test
    fun disableUserTest(){
        val user = createTestUser(true)
        user.inactivateUser()

        assertNull(user.age)
        assertNull(user.gender)
        assertNull(user.id)
        assertNull(user.nickname)
        assertFalse(user.isActivated)

    }

}
