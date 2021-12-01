package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryCreateResponse
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.service.StoryService
import mashup.spring.seehyang.service.auth.UserId
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
    @GetMapping("/story/{storyId}")
    fun getStory(
        @PathVariable("storyId") storyId : Long,
        @ApiIgnore userId: UserId?,
    ): SeehyangResponse<StoryDto> {

        val storyDto = storyService.getStoryDetail(storyId, userId)

        return SeehyangResponse(storyDto)
    }


    /**
     * 2. 향수 id 로 여러개 가져오기
     */
    @GetMapping("/perfume/{perfumeId}/story")
    fun getStoryByPerfume(
        @ApiIgnore userId: UserId?,
        @PathVariable(value = "perfumeId") perfumeId: Long,
        @RequestParam(value = "cursor", required = false)cursor: Long? = null
    ): SeehyangResponse<List<StoryDto>>{

        val storyListDto = storyService.getStoriesByPerfume(perfumeId, userId, cursor)

        return SeehyangResponse(storyListDto)
    }

    @PostMapping("/story")
    fun createStory(
        @RequestBody createRequest: StoryCreateRequest,
        @ApiIgnore userId: UserId?,
    ) : SeehyangResponse<StoryCreateResponse> {

        if(userId == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val storyDto = storyService.createStory(userId, createRequest)

        return SeehyangResponse(StoryCreateResponse(storyDto))
    }



    @PostMapping("/story/{storyId}/like")
    fun likeStory(
        @ApiIgnore userId: UserId?,
        @PathVariable("storyId") storyId : Long,
    ): SeehyangResponse<Map<String, Boolean>> {

        if(userId == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val isLike = storyService.likeStory(userId, storyId)

        return SeehyangResponse(mutableMapOf(Pair("isLike", isLike)))
    }

    @DeleteMapping("/story/{id}")
    fun deleteStory(
        @ApiIgnore userId: UserId?,
        @PathVariable id: Long
    ): SeehyangResponse<Map<String, Long>>{

        if(userId == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val deletedId = storyService.deleteStory(id,userId)

        return SeehyangResponse(mutableMapOf(Pair("id", deletedId)))
    }

}