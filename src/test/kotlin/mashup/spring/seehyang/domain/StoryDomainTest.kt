package mashup.spring.seehyang.domain

import mashup.spring.seehyang.createTestBrand
import mashup.spring.seehyang.createTestPerfume
import mashup.spring.seehyang.createTestStory
import mashup.spring.seehyang.createTestUser
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.user.OAuthType
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.CommentRepository
import mashup.spring.seehyang.repository.community.StoryLikeRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.community.TagRepository
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeLikeRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager

@DataJpaTest
class StoryDomainTest @Autowired constructor(
    val perfumeRepository: PerfumeRepository,
    val perfumeLikeRepository: PerfumeLikeRepository,
    val imageRepository: ImageRepository,
    val userRepository: UserRepository,
    val storyRepository: StoryRepository,
    val brandRepository: BrandRepository,
    val entityManager: EntityManager,
    val tagRepository: TagRepository,
    val commentRepository: CommentRepository,
    val storyLikeRepository: StoryLikeRepository
) {

    private val storyDomain = StoryDomain(storyRepository, storyLikeRepository, commentRepository)

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
    @DisplayName("private story 하나만 있는 경우, 작성자가 조회")
    fun countStoriesByPerfumeTest(){
        val user = userRepository.findAll()[0]
        val perfume = perfumeRepository.findAll()[0]
        val privateStory = createStory(isOnlyMe = true)
        storyRepository.save(privateStory)

        entityManager.flush()
        entityManager.clear()

        val count = storyDomain.countStoriesByPerfume(perfume.id!!, user)
        assertThat(count).isEqualTo(1)
    }

    @Test
    @DisplayName("public story 하나만 있는 경우, 작성자가 조회")
    fun countStoriesByPerfumeTest2(){
        val user = userRepository.findAll()[0]
        val perfume = perfumeRepository.findAll()[0]
        val publicStory = createStory(isOnlyMe = false)
        storyRepository.save(publicStory)

        entityManager.flush()
        entityManager.clear()

        val count = storyDomain.countStoriesByPerfume(perfume.id!!, user)
        assertThat(count).isEqualTo(1)
    }

    @Test
    @DisplayName("public story 두개만 있는 경우, 작성자가 조회")
    fun countStoriesByPerfumeTest3(){
        val user = userRepository.findAll()[0]
        val perfume = perfumeRepository.findAll()[0]
        val publicStory = createStory(isOnlyMe = false)
        val publicStory2 = createStory(isOnlyMe = false)
        storyRepository.save(publicStory)
        storyRepository.save(publicStory2)

        entityManager.flush()
        entityManager.clear()

        val count = storyDomain.countStoriesByPerfume(perfume.id!!, user)
        assertThat(count).isEqualTo(2)
    }

    @Test
    @DisplayName("public story 두개, private 하나 있는 경우 유저 정보 없이 조회")
    fun countStoriesByPerfumeTest4(){
        val user = userRepository.findAll()[0]
        val perfume = perfumeRepository.findAll()[0]
        val publicStory = createStory(isOnlyMe = false)
        val publicStory2 = createStory(isOnlyMe = false)
        val privateStory = createStory(isOnlyMe = true)
        storyRepository.save(publicStory)
        storyRepository.save(publicStory2)
        storyRepository.save(privateStory)

        entityManager.flush()
        entityManager.clear()

        val count = storyDomain.countStoriesByPerfume(perfume.id!!, null)
        assertThat(count).isEqualTo(2)
    }

    @Test
    @DisplayName("public story 두개, private 하나 있는 경우 private 작성자 아닌 유저가 조회")
    fun countStoriesByPerfumeTest5(){
        val user = User("test123@naver.com", OAuthType.GOOGLE)
        userRepository.save(user)
        val perfume = perfumeRepository.findAll()[0]
        val publicStory = createStory(isOnlyMe = false)
        storyRepository.save(publicStory)
        storyRepository.save(publicStory)

        entityManager.flush()
        entityManager.clear()

        val count = storyDomain.countStoriesByPerfume(perfume.id!!, user)
        assertThat(count).isEqualTo(1)
    }

    //TODO: 모든 메서드 모든 조건 테스트 코드 작성하기

    private fun createStory(isOnlyMe: Boolean): Story {
        val image = imageRepository.findAll()[0]
        val perfume = perfumeRepository.findAll()[0]
        val user = userRepository.findAll()[0]

        return createTestStory(image = image, perfume = perfume, user = user,isOnlyMe = isOnlyMe)
    }
}