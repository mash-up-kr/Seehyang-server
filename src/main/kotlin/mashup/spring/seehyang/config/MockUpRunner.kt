package mashup.spring.seehyang.config

import mashup.spring.seehyang.domain.entity.perfume.*
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.repository.perfume.AccordRepository
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class MockUpRunner (
    private val userRepository : UserRepository,
    private val brandRepository: BrandRepository,
    private val accordRepository: AccordRepository
) : ApplicationRunner{

    override fun run(args: ApplicationArguments?) {

        /**
         * sample user 1,2
         */
        val adam = User(gender = Gender.M, age = 25, nickname = "Adam", email= "ILoveEve@naver.com")
        val eve = User(gender = Gender.W, age = 23, nickname = "Eve", email= "ILoveAdam@naver.com")
        userRepository.save(adam)
        userRepository.save(eve)


        /**
         * sample Brand
         */

        val brand = Brand(name = "Eden", koreanName = "에덴")
        brandRepository.save(brand)

        /**
         * sample Accord
         */
        val accord = Accord(name = "fruit", koreanName = "과일")
        accordRepository.save(accord)

        /**
         * sample perfume
         */
        //val perfume = Perfume(name = "Apple", koreanName = "사과향 향수", type = PerfumeType.EAU_DE, gender = Gender.BOTH, thumbnailUrl = "test")
    }
}