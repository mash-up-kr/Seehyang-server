package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryCreateResponse
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.dto.community.StoryListItemDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.service.StoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@ApiV1
class StoryApiController(
    private val storyService: StoryService
) {

    @GetMapping("/story/{id}")
    fun getStory(
        @PathVariable id : Long
    ): SeehyangResponse<StoryDto> {
        val story = storyService.getStoryDetail(id)
        return SeehyangResponse(StoryDto(story))
    }

    @PostMapping("/story")
    fun createStory(
        createRequest: StoryCreateRequest,
        user: User,
    ) : SeehyangResponse<StoryCreateResponse> {
        if(user.isLogin().not()) throw RuntimeException("Not Authorization user..")
        val story = storyService.create(user, createRequest)

        return SeehyangResponse(StoryCreateResponse(story))
    }

    @GetMapping("/perfume/{id}/story")
    fun getStoryByPerfume(
        @PathVariable(value = "id") perfumeId: Long,
        @RequestParam(value = "cursor") cursor: Long? = null
    ): SeehyangResponse<List<StoryListItemDto>>{
        val stories = storyService.getStories(perfumeId, cursor)
        val storyListDto = stories.map { StoryListItemDto(it) }
        return SeehyangResponse(storyListDto)
    }

    @PostMapping("/story/{id}/like")
    fun likeStory(
        user: User,
        @PathVariable id : Long,
    ): SeehyangResponse<Map<String, Boolean>> {
        if(user.isLogin().not()) throw RuntimeException("Not Authorization user..")
        val isLike = storyService.likeStory(user, id)

        return SeehyangResponse(mutableMapOf(Pair("isLike", isLike)))
    }

}