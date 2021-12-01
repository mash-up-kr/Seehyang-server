package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.community.CommentDto
import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.PerfumeDomain
import mashup.spring.seehyang.domain.StoryDomain
import mashup.spring.seehyang.domain.TagDomain
import mashup.spring.seehyang.domain.UserDomain
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.repository.ImageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class StoryService(
    val imageRepository: ImageRepository,
    val perfumeRepository: PerfumeDomain,
    val storyDomain: StoryDomain,
    val tagDomain: TagDomain,
    val userDomain: UserDomain
) {

    private val PAGE_SIZE: Int = 10
    private val CANNOT_REPLY_TO_REPLY = NotFoundException(SeehyangStatus.INVALID_COMMENT_REPLY_REQUEST)
    private val UNAUTHORIZED_STORY_ACCESS = UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)

    /**
     * ============= Story 조회 ===============
     */

    @Transactional(readOnly = true)
    fun getStoryDetail(storyId: Long, userDto: UserDto?): StoryDto {

        var user: User? = null

        if (userDto != null) {
            user = userDomain.getLoginUser(userDto)
        }

        val story = storyDomain.getStoryById(storyId, user)

        return getStoryDtoWithIsLiked(story, user)
    }

    @Transactional(readOnly = true)
    fun getStoriesByPerfume(perfumeId: Long, userDto: UserDto?, cursor: Long?): List<StoryDto> {

        var user: User? = null

        if (userDto != null) {
            user = userDomain.getLoginUser(userDto)
        }

        val stories = storyDomain.getStoriesByPerfume(
            perfumeId = perfumeId,
            user = user,
            pageSize = 10,
            cursor = cursor
        )


        return getStoryDtosWithIsLiked(stories, user)
    }


    /**
     * =============== 스토리 생성, 수정, 삭제=================
     */
    fun createStory(userDto: UserDto, storyCreateRequest: StoryCreateRequest): StoryDto {

        val user = userDomain.getLoginUser(userDto)
        val perfume = perfumeRepository.getPerfume(storyCreateRequest.perfumeId)
        val image = imageRepository.findById(storyCreateRequest.imageId).get()
        val tags = storyCreateRequest.tags

        val savedStory = storyDomain.createStory(
            storyCreateRequest = storyCreateRequest,
            user = user,
            perfume = perfume,
            image = image
        )

        tagDomain.addTagsToStory(savedStory, tags)

        return StoryDto(savedStory)
    }

    fun likeStory(userDto: UserDto, storyId: Long): Boolean {

        val user = userDomain.getLoginUser(userDto)

        val story = storyDomain.getStoryById(storyId, user)
        val currentLikeState = story.likeStory(user ?: throw UNAUTHORIZED_STORY_ACCESS)

        return currentLikeState


    }

    fun deleteStory(storyId: Long, userDto: UserDto): Long {

        val user = userDomain.getLoginUser(userDto)

        return storyDomain.deleteStory(storyId, user)
    }

    /**
     * ================== Comment ====================
     */


    fun getComments(storyId: Long, userDto: UserDto?, cursor: Long?): List<CommentDto> {

        var user: User? = null

        if (userDto != null) {
            user = userDomain.getLoginUser(userDto)
        }

        return storyDomain.getComments(storyId, user, cursor).map { CommentDto(it) }

    }

    fun addComment(storyId: Long, userDto: UserDto, contents: String) {

        val user = userDomain.getLoginUser(userDto)

        val story = storyDomain.getStoryById(storyId, user)

        story.addComment(contents, user ?: throw UNAUTHORIZED_STORY_ACCESS)
    }

    fun deleteComment(storyId: Long, commentId: Long, userDto: UserDto) {

        val user = userDomain.getLoginUser(userDto)

        val story = storyDomain.getStoryById(storyId, user)

        story.deleteComment(commentId, user ?: throw UNAUTHORIZED_STORY_ACCESS)

    }

    fun deleteReplyComment(storyId: Long, commentId: Long, userDto: UserDto, contents: String) {
        val user = userDomain.getLoginUser(userDto)
        val story = storyDomain.getStoryById(storyId, user ?: throw UNAUTHORIZED_STORY_ACCESS)
        story.deleteReplyComment(commentId, user ?: throw UNAUTHORIZED_STORY_ACCESS)
    }


    /**
     * ================== Reply Comment ====================
     */


    fun getReplyComments(storyId: Long, commentId: Long, userDto: UserDto?, cursor: Long?): List<CommentDto> {

        var user: User? = null

        if (userDto != null) {
            user = userDomain.getLoginUser(userDto)
        }

        return storyDomain.getReplyComments(storyId, commentId, user, cursor).map { CommentDto(it) }
    }


    fun addReplyComment(storyId: Long, commentId: Long, userDto: UserDto, contents: String) {

        val user = userDomain.getLoginUser(userDto)
        val story = storyDomain.getStoryById(storyId, user)

        val isReplyAdded = story.addReplyComment(commentId, contents, user ?: throw UNAUTHORIZED_STORY_ACCESS)

        if (isReplyAdded.not()) {
            throw CANNOT_REPLY_TO_REPLY
        }
    }


    fun deleteReplyComment(storyId: Long, commentId: Long, userDto: UserDto) {
        val user = userDomain.getLoginUser(userDto)
        val story = storyDomain.getStoryById(storyId, user)

        story.deleteReplyComment(commentId, user ?: throw UNAUTHORIZED_STORY_ACCESS)
    }

    /**
     * ============ Private Methods =============
     */

    fun getStoryDtoWithIsLiked(story: Story, user: User?): StoryDto {
        val storyDto = StoryDto(story)

        if (user != null) {
            storyDto.isLiked = story.isUserLike(user)
        } else {
            storyDto.isLiked = false
        }

        return storyDto
    }

    fun getStoryDtosWithIsLiked(stories: List<Story>, user: User?): List<StoryDto> {

        return if (user != null) {
            stories.map {
                val storyDto = StoryDto(it)
                storyDto.isLiked = it.isUserLike(user)
                return@map storyDto
            }.toList()
        } else {
            stories.map {
                val storyDto = StoryDto(it)
                storyDto.isLiked = false
                return@map storyDto
            }.toList()
        }
    }

}