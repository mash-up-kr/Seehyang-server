package mashup.spring.seehyang.domain.entity

import mashup.spring.seehyang.createTestBrand
import mashup.spring.seehyang.createTestPerfume
import mashup.spring.seehyang.createTestStory
import mashup.spring.seehyang.createTestUser
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.community.StoryTag
import mashup.spring.seehyang.domain.entity.community.Tag
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.community.TagRepository
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

@DataJpaTest
class TagTest @Autowired constructor (
    val tagRepository: TagRepository,
    val storyRepository: StoryRepository,
    val imageRepository: ImageRepository,
    val userRepository: UserRepository,
    val perfumeRepository: PerfumeRepository,
    val brandRepository: BrandRepository
){

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
        tagRepository.save(Tag(contents = "TestTag"))
    }

    @Test
    @DisplayName("add Story Tag 중복 제거 테스트")
    fun addTagTest(){

        val story = createStory()
        val tag = tagRepository.findByContents("TestTag")!!
        val storyTag = StoryTag(tag = tag, story = story)

        tag.addStoryTag(storyTag)
        tag.addStoryTag(storyTag)

        assertEquals(tag.viewStoryTag().size, 1)

    }

    private fun createStory(): Story {
        val image = imageRepository.findAll()[0]
        val perfume = perfumeRepository.findAll()[0]
        val user = userRepository.findAll()[0]

        return createTestStory(image = image, perfume = perfume, user = user)
    }
}