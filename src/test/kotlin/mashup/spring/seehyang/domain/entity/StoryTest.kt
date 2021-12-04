package mashup.spring.seehyang.domain.entity

import mashup.spring.seehyang.createTestBrand
import mashup.spring.seehyang.createTestPerfume
import mashup.spring.seehyang.createTestStory
import mashup.spring.seehyang.createTestUser
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.community.StoryTag
import mashup.spring.seehyang.domain.entity.community.Tag
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach

@DataJpaTest
class StoryTest @Autowired constructor (
    val imageRepository: ImageRepository,
    val userRepository: UserRepository,
    val storyRepository: StoryRepository,
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
    }

    /**
     * 테스트용 스토리를 만드는 함수를 테스트한다.
     */
    @Test
    fun createTestStoryTest(){

        val testStory = createStory()

        assertEquals(testStory.commentCount, 0)
        assertEquals(testStory.likeCount, 0)
        assertEquals(testStory.viewStoryTags().size, 0)
    }

    /**
     * story Like Test
     */
    @Test
    fun likeStoryTest(){
        val user = userRepository.findAll()[0]
        val story = createStory()


        for(i in 1..3){
            story.likeStory(user)
        }
        //좋아요-좋아요취소-좋아요
        assertEquals(story.likeCount, 1)
    }

    @Test
    fun addStoryTagTest(){
        val story = createStory()

        story.addStoryTag(StoryTag(tag = Tag(contents = "testTag1"), story = story))
        story.addStoryTag(StoryTag(tag = Tag(contents = "testTag2"), story = story))
        story.addStoryTag(StoryTag(tag = Tag(contents = "testTag3"), story = story))

        assertEquals(story.viewStoryTags().size, 3)
    }

    @Test
    fun deleteStoryTagTest(){
        val user = createTestUser(false)
        val story = createStory()

        val tag1 = Tag(contents = "testTag1")
        val tag2 = Tag(contents = "testTag2")
        val tag3 = Tag(contents = "testTag3")

        val storyTag1 = StoryTag(tag = tag1, story = story)
        val storyTag2 = StoryTag(tag = tag2, story = story)
        val storyTag3 = StoryTag(tag = tag3, story = story)

        story.addStoryTag(storyTag1)
        story.addStoryTag(storyTag2)
        story.addStoryTag(storyTag3)

        val savedStory = storyRepository.save(story)

        savedStory.deleteStoryTag(storyTag1)

        assertEquals(savedStory.viewStoryTags().size, 2)
    }

    @Test
    fun saveStoryTest(){
        val story = createStory()

        storyRepository.save(story)
        val storyUser = userRepository.save(story.user)

        assertNotNull(storyUser.id)
        assertNotNull(story.id)
    }

    @Test
    fun addCommentTest(){
        val story = createStory()

        storyRepository.save(story)
        val user = userRepository.save(story.user)

        val comment1 = story.addComment("testComment1", user)
        val comment2 = story.addComment("testComment2", user)

        val viewComments = story.viewComments()

        assertEquals(story.commentCount, 2)
        assertThat(viewComments).contains(comment1)
        assertThat(viewComments).contains(comment2)

    }

    @Test
    fun saveCommentTest(){
        val story = createStory()
        val user = userRepository.save(story.user)
        val comment2 = story.addComment("testComment2", user)
        val comment1 = story.addComment("testComment1", user)
        storyRepository.save(story)

        val foundStory = storyRepository.findById(story.id!!).get()

        assertThat(foundStory.viewComments()).contains(comment1, comment2)
    }


    private fun createStory(): Story {
        val image = imageRepository.findAll()[0]
        val perfume = perfumeRepository.findAll()[0]
        val user = userRepository.findAll()[0]

        return createTestStory(image = image, perfume = perfume, user = user)
    }


}