package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.community.Comment
import mashup.spring.seehyang.domain.entity.community.StoryLike
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.StoryLikeRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.community.StoryTagRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class StoryService(
    val storyLikeRepository: StoryLikeRepository,
    val storyRepository: StoryRepository,
    val imageRepository: ImageRepository,
    val perfumeRepository: PerfumeRepository,
    val tagService: TagService,
    val userService: UserService
) {

    private val PAGE_SIZE: Int = 10

    @Transactional(readOnly = true)
    fun getAdminStoryDetail(id: Long): Story = storyRepository.findById(id).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_STORY) }

    @Transactional(readOnly = true)
    fun getStoryDetail(user: User,id: Long): StoryDto {
        val story = storyRepository.findById(id).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_STORY) }
        validateOnlyMe(user, story)
        return StoryDto(story, isLiked = story.storyLikes.any { storyLike -> storyLike.user.id == user.id })
    }

    @Transactional(readOnly = true)
    fun getStoriesByPerfume(user: User, perfumeId: Long, cursor: Long?): List<StoryDto>
    = if (cursor == null) {
            storyRepository.findTop10ByPerfumeIdOrderByIdDesc(perfumeId)
                .filterNot {
                    it.isOnlyMe && it.user.id != user.id
                }.map { StoryDto(it, isLiked = it.storyLikes.any { storyLike -> storyLike.user.id == user.id }) }.toList()
        } else {
            storyRepository.findStoryByPerfumeId(perfumeId, cursor, PageRequest.ofSize(PAGE_SIZE))
                .filterNot {
                    it.isOnlyMe && it.user.id != user.id
                }.map { StoryDto(it, isLiked = it.storyLikes.any { storyLike -> storyLike.user.id == user.id }) }.toList()
        }

    /**
     * StoryCreateRequest
     * perfumeId : Long
     * imageId : Long
     * tags : MutableList<String>
     */
    fun create(user: User, storyCreateRequest: StoryCreateRequest): StoryDto {

        val managedUser = userService.getUser(user.id)

        // TODO : Entity Not Found 에러 핸들링
        val perfume = perfumeRepository.findById(storyCreateRequest.perfumeId).get()
        val image = imageRepository.findById(storyCreateRequest.imageId).get()
        val tags = storyCreateRequest.tags
        val isOnlyMe = storyCreateRequest.isOnlyMe

        val story = Story(
            perfume = perfume,
            user = managedUser,
            image = image,
            isOnlyMe = isOnlyMe
        )

        val savedStory = storyRepository.save(story)

        tagService.addTagsToStory(savedStory, tags)

        return StoryDto(savedStory)
    }

    fun likeStory(user: User, storyId: Long): Boolean {

        val managedUser = userService.getUser(user.id)
        val story = storyRepository.findById(storyId).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_STORY) }

        validateOnlyMe(managedUser,story)

        val like = storyLikeRepository.findByUserAndStory(managedUser, story)

        return if (like.isPresent) {
            cancelLike(like.get())
            false
        } else {
            saveStoryLike(managedUser, story)
            true
        }
    }

    fun deleteStory(user: User, id:Long): Long{
        val managedUser = userService.getUser(user.id)

        val userId = managedUser.id!!
        val userIdInStory = storyRepository.findById(id).orElseThrow{NotFoundException(SeehyangStatus.NOT_FOUND_STORY)}.user.id!!

        if (userId == userIdInStory) {
            storyRepository.deleteById(id)
            return id;
        } else {
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }
    }

    fun addComment(comment:Comment){
        val story = comment.story
        story.comments.add(comment)
        story.commentCount++
    }



    private fun cancelLike(storyLike: StoryLike){
        val story = storyLike.story
        storyLikeRepository.deleteById(storyLike.id?:throw NotFoundException(SeehyangStatus.NOT_FOUND_STORY))
        story.cancleLike()
    }

    private fun saveStoryLike(user:User, story:Story){
        storyLikeRepository.save(StoryLike(user = user, story = story))
        story.like()
    }

    fun validateOnlyMe(user:User, story:Story) {
        val isMine = user.isLogin() && (user.id!! == story.user.id)

        if (story.isOnlyMe && isMine.not()) {
            throw NotFoundException(SeehyangStatus.NOT_FOUND_STORY)
        }
    }
}