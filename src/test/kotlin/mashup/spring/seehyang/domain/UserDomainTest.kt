package mashup.spring.seehyang.domain

import mashup.spring.seehyang.createTestBrand
import mashup.spring.seehyang.createTestPerfume
import mashup.spring.seehyang.createTestStory
import mashup.spring.seehyang.createTestUser
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.community.TagRepository
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeLikeRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.service.auth.UserId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.EntityManager
import org.junit.jupiter.api.Assertions.*
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserDomainTest @Autowired constructor(
    val perfumeRepository: PerfumeRepository,
    val perfumeLikeRepository: PerfumeLikeRepository,
    val imageRepository: ImageRepository,
    val userRepository: UserRepository,
    val storyRepository: StoryRepository,
    val brandRepository: BrandRepository,
    val entityManager: EntityManager,
    val tagRepository: TagRepository,
) {

    private val userDomain = UserDomain(userRepository)

    @BeforeEach
    fun before(){
        //set other entities
        val image = Image(url = "test")
        val brand = createTestBrand()
        val perfume = createTestPerfume(brand)
        val user = createTestUser(true)

        imageRepository.save(image)
        brandRepository.save(brand)
        perfumeRepository.save(perfume)
        userRepository.save(user)
    }

    @Test
    fun getLoginUserTest(){
        val user = userRepository.findAll()[0]
        val userId = user.id!!
        entityManager.flush()
        entityManager.clear()

        val loginUser = userDomain.getLoginUser(UserId(userId))!!

        assertThat(loginUser.id!!).isEqualTo(userId)
    }

    @Test
    fun getLoginUserNotFoundTest(){

        val invalidId:Long = -1

        assertThrows(NotFoundException::class.java){userDomain.getLoginUser(UserId(invalidId))}
    }

    @Test
    fun getInactivateUserTest(){
        val user = userRepository.findAll()[0]
        val userId = user.id!!
        user.inactivateUser()

        entityManager.flush()
        entityManager.clear()

        val loginUser = userDomain.getLoginUser(UserId(userId))

        assertThat(loginUser).isNull()
    }



    private fun createStory(): Story {
        val image = imageRepository.findAll()[0]
        val perfume = perfumeRepository.findAll()[0]
        val user = userRepository.findAll()[0]

        return createTestStory(image = image, perfume = perfume, user = user)
    }
}