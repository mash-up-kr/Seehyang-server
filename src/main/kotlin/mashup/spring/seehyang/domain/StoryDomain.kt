package mashup.spring.seehyang.domain

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.Comment
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.enums.Domain
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.InternalServerException
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.repository.community.CommentRepository
import mashup.spring.seehyang.repository.community.StoryLikeRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.service.auth.UserId
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.time.LocalDateTime
import java.time.Month

@Domain
class StoryDomain(
    private val storyRepository: StoryRepository,
    private val storyLikeRepository: StoryLikeRepository,
    private val commentRepository: CommentRepository
) {

    /**
     * =========== Exceptions ==================
     */
    private val STORY_NOT_FOUND_EXCEPTION = NotFoundException(SeehyangStatus.NOT_FOUND_STORY)
    private val COMMENT_NOT_FOUND_EXCEPTION = NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT)
    private val UNAUTHORIZED_STORY_ACCESS = UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
    private val INVALID_USER_ENTITY = InternalServerException(SeehyangStatus.INVALID_USER_ENTITY)


    /**
     * =========== Responsibilities =============
     */

    fun createStory(storyCreateRequest: StoryCreateRequest, user: User?, perfume: Perfume, image: Image): Story {

        val isOnlyMe = storyCreateRequest.isOnlyMe

        val story = Story(isOnlyMe = isOnlyMe, user = user ?: throw UNAUTHORIZED_STORY_ACCESS, perfume = perfume, image = image)

        return storyRepository.save(story)

    }

    // Story ID??? ?????? ??????
    fun getStoryById(storyId: Long, user: User?): Story {

        val story = if (user != null) {
            storyRepository.findAccessibleStoryWithUser(
                storyId = storyId,
                userId = user.id ?: throw INVALID_USER_ENTITY
            ) ?: throw STORY_NOT_FOUND_EXCEPTION
        } else {
            storyRepository.findPublicStory(
                storyId = storyId
            ) ?: throw STORY_NOT_FOUND_EXCEPTION
        }

        //????????? ???????????? ?????? ?????? ?????? ??????
        validateAccessibility(story, user)

        return story
    }

    fun countStoriesByPerfume(perfumeId : Long, user: User?): Long{
        val fast = LocalDateTime.of(1970, Month.JANUARY,1,0,0)
        return if(user == null){
            storyRepository.countPublicStoriesByPerfumeId(perfumeId, from = fast, to = LocalDateTime.now())
        }else{
            storyRepository.countStoriesByPerfumeIdWithUser(perfumeId, user.id?: throw INVALID_USER_ENTITY, from = fast, to = LocalDateTime.now())
        }

    }

    // ????????? Story ID ??? ????????? ??????
    fun getStoriesByIds(storyIds: List<Long>, user: User?): List<Story> {

        val stories = if (user != null) {
            storyRepository.findAccessibleStoriesWithUser(
                storyIds = storyIds,
                userId = user.id ?: throw INVALID_USER_ENTITY
            )
        } else {
            storyRepository.findPublicStories(storyIds = storyIds)
        }

        //????????? ???????????? ?????? ?????? ?????? ??????
        validateAccessibility(stories, user)

        return stories
    }

    // ?????? ???????????? ????????? ?????? 10??? ????????????.
    fun getTopTenOrderByLike(): List<Story> {

        val firstPageNumber = 0
        val pageSize = 10
        val sort = Sort.by(Sort.Direction.DESC, "likeCount")
        val stories = storyRepository.findPublicStories(PageRequest.of(firstPageNumber, pageSize, sort))

        //????????? ???????????? ?????? ?????? ?????? ??????
        validateAccessibility(stories, null)

        return stories
    }


    // ?????? Perfume ??? ?????? ????????? ?????? ?????? ????????????
    fun getStoriesByPerfume(
        perfumeId: Long,
        user: User?,
        pageSize: Int,
        sortType: StorySortType = StorySortType.ID,
        cursor: Long?
    ): List<Story> {


        val stories = getSortedStories(
            perfumeId = perfumeId,
            user = user,
            pageSize = pageSize,
            cursor = cursor ?: Long.MAX_VALUE,
            sortType = sortType
        )

        //????????? ???????????? ?????? ?????? ?????? ??????
        validateAccessibility(stories, user)

        return stories
    }

    fun deleteStory(storyId: Long, user: User?): Long {

        val story = getStoryById(storyId, user)

        validateDeleteAccess(story, user ?: throw UNAUTHORIZED_STORY_ACCESS)

        storyRepository.deleteById(storyId)

        return storyId
    }


    // ?????????
    private val COMMENT_PAGE_SIZE: Int = 20

    fun getComments(storyId: Long, user: User?, cursor: Long?): List<Comment> {

        val story = getStoryById(storyId, user)

        return if (cursor == null) {
            commentRepository.findTop20ByStoryIdOrderByIdDesc(story.id!!)
        } else {
            commentRepository.findCommentsByStoryId(story, cursor, PageRequest.ofSize(COMMENT_PAGE_SIZE))
        }

    }

    fun getReplyComments(storyId: Long, commentId: Long, user: User?, cursor: Long?): List<Comment> {

        val story = getStoryById(storyId, user)

        val parentComment = story.viewComments().find { it.id == commentId } ?: throw COMMENT_NOT_FOUND_EXCEPTION

        return commentRepository.findReplyCommentsByParentId(
            parentComment.id!!,
            cursor ?: Long.MAX_VALUE,
            Pageable.ofSize(COMMENT_PAGE_SIZE)
        )
    }


    //????????? ???????????? ?????????
    fun getStoryIdByRecentLike(from: LocalDateTime, to: LocalDateTime, size: Int): List<Long> {

        val stories = storyLikeRepository.findStoryIdByRecentLike(
            from = from,
            to = to,
            PageRequest.ofSize(size)
        )

        validateAccessibility(stories, null)

        return stories.map { it.id?:throw InternalServerException(SeehyangStatus.INVALID_STORY_ENTITY) }
    }



    /**
     * ============= Private Methods ==================
     */


    // ?????? ????????? ???????????? ????????????.
    // ????????? ?????? ????????? sortType ?????? ??????
    private fun getSortedStories(
        perfumeId: Long,
        user: User?,
        pageSize: Int,
        sortType: StorySortType,
        cursor: Long
    ): List<Story> {
        //?????? ???????????? + ????????????
        val firstPageNumber = 0
        val pageable = PageRequest.of(firstPageNumber, pageSize, Sort.by(sortType.fieldName).descending())

        return if (user != null) {
            storyRepository.findAccessibleStoriesWithUserByPerfumeId(perfumeId, user.id ?: throw INVALID_USER_ENTITY, cursor, pageable)
        } else {
            storyRepository.findPublicStoriesByPerfumeId(perfumeId, cursor, pageable)
        }
    }


    private fun validateAccessibility(story: Story, user: User?) {

        val userIdInStory = story.user.id
        val userIdInDto = user?.id

        if (isAccessible(story.isOnlyMe, userIdInDto, userIdInStory).not()) {
            throw STORY_NOT_FOUND_EXCEPTION
        }
    }

    private fun validateAccessibility(stories: List<Story>, user: User?) {

        stories.forEach {
            val userIdInStory = it.user.id
            val userIdInDto = user?.id

            if (isAccessible(it.isOnlyMe, userIdInDto, userIdInStory).not()) {
                throw STORY_NOT_FOUND_EXCEPTION
            }
        }
    }

    private fun isAccessible(isOnlyMe: Boolean, idInDto: Long?, idInStory: Long?): Boolean {

        return if (idInDto != null && idInStory != null) (isOnlyMe && idInDto != idInStory).not() else isOnlyMe.not()
    }

    private fun validateDeleteAccess(story: Story, user: User) {

        val userIdInStory = story.user.id ?: throw UNAUTHORIZED_STORY_ACCESS
        val userId = user.id ?: throw INVALID_USER_ENTITY

        if (userId != userIdInStory) {
            throw UNAUTHORIZED_STORY_ACCESS
        }
    }


}
