package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
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
    val userRepository: UserRepository
) {

    private val PAGE_SIZE: Int = 20

    fun getStoryDetail(id: Long): Story {
        return storyRepository.findById(id).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_STORY) }
    }

    /**
     * StoryCreateRequest
     * perfumeId : Long
     * imageId : Long
     * tags : MutableList<String>
     */
    fun create(user: User, storyCreateRequest: StoryCreateRequest): StoryDto {

        val managedUser = userRepository.findById(user.id?:throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER))
            .orElseThrow{NotFoundException(SeehyangStatus.NOT_FOUND_USER)}

        // TODO : Entity Not Found 에러 핸들링
        val perfume = perfumeRepository.findById(storyCreateRequest.perfumeId).get()
        val image = imageRepository.findById(storyCreateRequest.imageId).get()
        val tags = storyCreateRequest.tags

        val story = Story(
            perfume = perfume,
            user = managedUser,
            image = image
        )

        val savedStory = storyRepository.save(story)

        tagService.addTagsToStory(savedStory, tags)

        return StoryDto(savedStory)
    }


    @Transactional(readOnly = true)
    fun getStoriesByPerfume(perfumeId: Long, cursor: Long?): List<Story>
        = if (cursor == null) storyRepository.findTop20ByPerfumeIdOrderByIdDesc(perfumeId)
            else storyRepository.findStoryByPerfumeId(perfumeId, cursor, PAGE_SIZE)

    fun likeStory(user: User, storyId: Long): Boolean {
        val managedUser = userRepository.findById(user.id?:throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER))
            .orElseThrow{NotFoundException(SeehyangStatus.NOT_FOUND_USER)}

        val story = storyRepository.findById(storyId).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_STORY) }
        val like = storyLikeRepository.findByUserAndStory(managedUser, story)

        return if (like.isPresent) {
            storyLikeRepository.delete(like.get())
            story.cancleLike()
            false
        } else {
            storyLikeRepository.save(StoryLike(user = managedUser, story = story))
            story.like()
            true
        }
    }

    fun deleteStory(user: User, id:Long): Long{
        val managedUser = userRepository.findById(user.id?:throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER))
            .orElseThrow{NotFoundException(SeehyangStatus.NOT_FOUND_USER)}

        val userId = managedUser.id!!
        val userIdInStory = storyRepository.findById(id).orElseThrow{NotFoundException(SeehyangStatus.NOT_FOUND_STORY)}.id!!
        if (userId == userIdInStory) {
            storyRepository.deleteById(id)
            return id;
        } else {
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }
    }

}