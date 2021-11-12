package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryCreateResponse
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.service.StoryService
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

/**
 * Story Api Controller
 * C: 1. 향수 업로드  (Comment -> Comment API Controller)
 * R: 1. 스토리 id로 하나 가져오기 2. 향수 Id 로 여러개 가져오기
 * U: 1. 향수 좋아요.
 * D:
 */
@ApiV1
class StoryApiController(
    private val storyService: StoryService
) {

    /**
     * 1. id로 하나 가져오기
     */
    @GetMapping("/story/{id}")
    fun getStory(
        @PathVariable id : Long,
        @ApiIgnore user: User,
    ): SeehyangResponse<StoryDto> {
        val story = storyService.getStoryDetail(user,id)
        return SeehyangResponse(story)
    }


    /**
     * 2. 향수 id 로 여러개 가져오기
     */
    @GetMapping("/perfume/{id}/story")
    fun getStoryByPerfume(
        @ApiIgnore user: User,
        @PathVariable(value = "id") perfumeId: Long,
        @RequestParam(value = "cursor", required = false)cursor: Long? = null
    ): SeehyangResponse<List<StoryDto>>{
        val storyListDto = storyService.getStoriesByPerfume(user, perfumeId, cursor)
        return SeehyangResponse(storyListDto)
    }

    @PostMapping("/story")
    fun createStory(
        @RequestBody createRequest: StoryCreateRequest,
        @ApiIgnore user: User,
    ) : SeehyangResponse<StoryCreateResponse> {
        if(user.isLogin().not())
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        val storyDto = storyService.create(user, createRequest)

        return SeehyangResponse(StoryCreateResponse(storyDto))
    }



    @PostMapping("/story/{id}/like")
    fun likeStory(
        @ApiIgnore user: User,
        @PathVariable id : Long,
    ): SeehyangResponse<Map<String, Boolean>> {
        if(user.isLogin().not())
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        val isLike = storyService.likeStory(user, id)

        return SeehyangResponse(mutableMapOf(Pair("isLike", isLike)))
    }

    @DeleteMapping("/story/{id}")
    fun deleteStory(
        @ApiIgnore user: User,
        @PathVariable id: Long
    ): SeehyangResponse<Map<String, Long>>{
        if(user.isLogin().not())
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        val deletedId = storyService.deleteStory(user,id)

        return SeehyangResponse(mutableMapOf(Pair("id", deletedId)))
    }

}