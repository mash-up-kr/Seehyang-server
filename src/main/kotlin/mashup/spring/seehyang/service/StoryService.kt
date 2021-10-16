package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryDetailDto
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.domain.entity.community.Like
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.LikeRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.community.StoryTagRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.streams.toList

@Transactional
@Service
class StoryService(
    val likeRepository: LikeRepository,
    val storyRepository: StoryRepository,
    val imageRepository: ImageRepository,
    val perfumeRepository: PerfumeRepository,
    val storyTagRepository: StoryTagRepository,
    val tagService: TagService
) {

    private val PAGE_SIZE: Int = 20

    fun getStoryDetail(id: Long): Story {
        return storyRepository.findById(id).get()
    }

    /**
     * StoryCreateRequest
     * perfumeId : Long
     * imageId : Long
     * tags : MutableList<String>
     */
    fun create(user: User, storyCreateRequest: StoryCreateRequest): StoryDto {

        // TODO : Entity Not Found 에러 핸들링
        val perfume = perfumeRepository.findById(storyCreateRequest.perfumeId).get()
        val image = imageRepository.findById(storyCreateRequest.imageId).get()
        val tags = storyCreateRequest.tags

        val story = Story(
            perfume = perfume,
            user = user,
            image = image
        )

        val savedStory = storyRepository.save(story)

        tagService.addTagsToStory(savedStory, tags)

        return StoryDto(savedStory)
    }

    @Transactional(readOnly = true)
    fun getStories(perfumeId: Long, cursor: Long?): List<Story>
        = if (cursor == null) storyRepository.findTop20ByPerfumeIdOrderByIdDesc(perfumeId)
            else storyRepository.findStoryByPerfumeId(perfumeId, cursor, PAGE_SIZE)

    fun likeStory(user: User, storyId: Long): Boolean {
        val story = storyRepository.findById(storyId).orElseThrow { RuntimeException("Entity Not Found : Story") }
        val like = likeRepository.findByUserAndStory(user, story)

        val isLike = if (like.isPresent) {
            likeRepository.delete(like.get())
            story.cancleLike()
            false
        } else {
            likeRepository.save(Like(user = user, story = story))
            story.like()
            true
        }

        return isLike
    }


//    @Transactional(readOnly = true)
//    fun getStories(perfumeId: Long, sorting: SortingType, pageable: Pageable): List<StoryDetailDto> {
//
//        val stories = when (sorting) {
//            SortingType.NEW -> storyRepository.findByPerfumeIdOrderByDate(perfumeId, pageable)
//            SortingType.COMMENT -> storyRepository.findByPerfumeIdOrderByComment(perfumeId, pageable)
//            SortingType.LIKE -> storyRepository.findByPerfumeIdOrderByLike(perfumeId, pageable)
//        }
//
//        return stories.stream()
//            .map { it ->
//                StoryDetailDto(
//                    id = it.id!!,
//                    userNickname = it.user.nickname,
//                    userProfileUrl = it.user.profileImage?.url ?: "",
//                    commentCount = it.commentCount,
//                    likeCount = it.likeCount,
//                    storyImageUrl = it.image.url,
//                    tags = storyTagRepository.findByStoryId(it.id!!)
//                                                .stream()
//                                                .map { it.tag.contents }
//                                                .toList()
//                )
//            }
//            .toList()
//    }

}