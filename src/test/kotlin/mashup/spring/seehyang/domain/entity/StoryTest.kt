package mashup.spring.seehyang.domain.entity

import mashup.spring.seehyang.createTestStory
import mashup.spring.seehyang.createTestUser
import mashup.spring.seehyang.domain.entity.community.StoryTag
import mashup.spring.seehyang.domain.entity.community.Tag
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat

@DataJpaTest
class StoryTest @Autowired constructor (
    val userRepository: UserRepository,
    val storyRepository: StoryRepository
){


    /**
     * 테스트용 스토리를 만드는 함수를 테스트한다.
     */
    @Test
    fun createTestStoryTest(){
        val isSetDefault = true

        val testStory = createTestStory(isSetDefault)

        assertEquals(testStory.commentCount, 1)
        assertEquals(testStory.likeCount, 1)
        assertEquals(testStory.viewStoryTags().size, 1)
    }

    /**
     * story Like Test
     */
    @Test
    fun likeStoryTest(){
        val user = createTestUser(false)
        val story = createTestStory(false)

        val savedUser = userRepository.save(user)

        for(i in 1..3){
            story.likeStory(savedUser)
        }
        //좋아요-좋아요취소-좋아요
        assertEquals(story.likeCount, 1)
    }

    @Test
    fun addStoryTagTest(){
        val story = createTestStory(false)

        story.addStoryTag(StoryTag(tag = Tag(contents = "testTag1"), story = story))
        story.addStoryTag(StoryTag(tag = Tag(contents = "testTag2"), story = story))
        story.addStoryTag(StoryTag(tag = Tag(contents = "testTag3"), story = story))

        assertEquals(story.viewStoryTags().size, 3)
    }

    @Test
    fun deleteStoryTagTest(){
        val user = createTestUser(false)
        val story = createTestStory(false)

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
        val story = createTestStory(false)

        storyRepository.save(story)
        val storyUser = userRepository.save(story.user)

        assertNotNull(storyUser.id)
        assertNotNull(story.id)
    }

    @Test
    fun addCommentTest(){
        val story = createTestStory(false)

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
        val story = createTestStory(false)
        val user = userRepository.save(story.user)
        val comment2 = story.addComment("testComment2", user)
        val comment1 = story.addComment("testComment1", user)
        storyRepository.save(story)

        val foundStory = storyRepository.findById(story.id!!).get()

        assertThat(foundStory.viewComments()).contains(comment1, comment2)
    }


}