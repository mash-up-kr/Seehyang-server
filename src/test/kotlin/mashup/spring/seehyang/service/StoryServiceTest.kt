package mashup.spring.seehyang.service

import mashup.spring.seehyang.createTestBrand
import mashup.spring.seehyang.createTestPerfume
import mashup.spring.seehyang.createTestStory
import mashup.spring.seehyang.createTestUser
import mashup.spring.seehyang.domain.StoryDomain
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.community.TagRepository
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeLikeRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.service.auth.UserId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class StoryServiceTest  @Autowired constructor(
    val perfumeRepository: PerfumeRepository,
    val imageRepository: ImageRepository,
    val userRepository: UserRepository,
    val storyRepository: StoryRepository,
    val brandRepository: BrandRepository,
    val entityManager: EntityManager,
    val storyService: StoryService
)  {
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
    @DisplayName("User Id 가 Null 이 아니고 좋아요 한 경우")
    fun getStoryDetailWithLikeTest(){
        val story = storyRepository.save(createStory())
        val storyId = story.id!!
        story.likeStory(story.user)
        val userId = story.user.id!!
        entityManager.flush()
        entityManager.clear()

        val foundStory = storyService.getStoryDetail(storyId, UserId(userId))

        assertThat(foundStory.id).isEqualTo(storyId)
        assertThat(foundStory.isLiked).isTrue
    }

    @Test
    @DisplayName("User Id 가 Null 이 아니고 좋아요하지 않은 경우")
    fun getStoryDetailWithOutLikeTest(){
        val story = storyRepository.save(createStory())
        val storyId = story.id!!
        val userId = story.user.id!!
        entityManager.flush()
        entityManager.clear()

        val foundStory = storyService.getStoryDetail(storyId, UserId(userId))

        assertThat(foundStory.id).isEqualTo(storyId)
        assertThat(foundStory.isLiked).isFalse
    }

    @Test
    @DisplayName("Image 에 prefix 잘 붙어서 오는지")
    fun getStoryDetailImageUrlTest(){
        val story = storyRepository.save(createStory())
        val storyId = story.id!!
        val userId = story.user.id!!
        entityManager.flush()
        entityManager.clear()

        val foundStory = storyService.getStoryDetail(storyId, UserId(userId))

        assertThat(foundStory.id).isEqualTo(storyId)
        assertThat(foundStory.imageUrl).contains("https://elasticbeanstalk-ap-northeast-2-306614265263.s3.ap-northeast-2.amazonaws.com/")
    }

    @Test
    @DisplayName("User Id 가 Null 인 경우")
    fun getStoryDetailWithOutUserTest(){
        val story = storyRepository.save(createStory())
        val storyId = story.id!!
        entityManager.flush()
        entityManager.clear()

        val foundStory = storyService.getStoryDetail(storyId, null)

        assertThat(foundStory.id).isEqualTo(storyId)
        assertThat(foundStory.isLiked).isFalse
    }
    @Test
    @DisplayName("")
    fun getStoriesByPerfumeTest(){
        //getStoriesByPerfume(perfumeId: Long, userId: UserId?, cursor: Long?)
        storyRepository.save(createStory())
        val story = storyRepository.findAll()[0]
        val perfumeId = story.perfume.id!!
        entityManager.flush()
        entityManager.clear()

        val stories = storyService.getStoriesByPerfume(perfumeId, null, null)
        assertThat(stories.size).isEqualTo(1)
        assertThat(stories[0].isLiked).isFalse
        assertThat(stories[0].id!!).isEqualTo(story.id!!)

    }

    //TODO: 모든 메서드 모든 조건 테스트 코드 작성하기

    private fun createStory(): Story {
        val image = imageRepository.findAll()[0]
        val perfume = perfumeRepository.findAll()[0]
        val user = userRepository.findAll()[0]

        return createTestStory(image = image, perfume = perfume, user = user)
    }
}