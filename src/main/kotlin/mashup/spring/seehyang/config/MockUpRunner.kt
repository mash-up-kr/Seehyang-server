package mashup.spring.seehyang.config

import mashup.spring.seehyang.domain.entity.perfume.Gender
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.repository.user.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class MockUpRunner (
    private val userRepository : UserRepository
) : ApplicationRunner{

    override fun run(args: ApplicationArguments?) {

        val adam = User(gender = Gender.M, age = 25, nickname = "Adam", email= "ILoveEve@naver.com")
        val eve = User(gender = Gender.W, age = 23, nickname = "Eve", email= "ILoveAdam@naver.com" )
        userRepository.save(adam)
        userRepository.save(eve)

    }
}