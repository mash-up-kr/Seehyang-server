package mashup.spring.seehyang.domain

import mashup.spring.seehyang.createTestBrand
import mashup.spring.seehyang.createTestPerfume
import mashup.spring.seehyang.createTestStory
import mashup.spring.seehyang.createTestUser
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.community.Tag
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.community.TagRepository
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeLikeRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager
import org.junit.jupiter.api.Assertions.*
import org.assertj.core.api.Assertions.assertThat



@DataJpaTest
class TagDomainTest @Autowired constructor(
    val perfumeRepository: PerfumeRepository,
    val perfumeLikeRepository: PerfumeLikeRepository,
    val imageRepository: ImageRepository,
    val userRepository: UserRepository,
    val storyRepository: StoryRepository,
    val brandRepository: BrandRepository,
    val entityManager: EntityManager,
    val tagRepository: TagRepository,
){
    val tagDomain = TagDomain(tagRepository)

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

    /**
     * ======= addTagsToStory Test ========
     */

    @Test
    fun addTagsToStoryTest(){
        tagRepository.saveAll(listOf(Tag(contents = "exist1"),Tag(contents = "exist2")))
        entityManager.flush()
        entityManager.clear()

        val story = createStory()
        tagDomain.addTagsToStory(story, listOf("exist1", "exist2","notExist"))

        assertThat(tagRepository.findAll().size).isEqualTo(3)
        assertThat(story.viewStoryTags().size).isEqualTo(3)

    }

    private fun createStory(): Story {
        val image = imageRepository.findAll()[0]
        val perfume = perfumeRepository.findAll()[0]
        val user = userRepository.findAll()[0]

        return createTestStory(image = image, perfume = perfume, user = user)
    }

}