package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryCreateResponse
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
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
    @GetMapping("/story/{storyId}")
    fun getStory(
        @PathVariable("storyId") storyId : Long,
        @ApiIgnore userDto: UserDto,
    ): SeehyangResponse<StoryDto> {

        val storyDto = storyService.getStoryDetail(storyId, userDto)

        return SeehyangResponse(storyDto)
    }


    /**
     * 2. 향수 id 로 여러개 가져오기
     */
    @GetMapping("/perfume/{perfumeId}/story")
    fun getStoryByPerfume(
        @ApiIgnore userDto: UserDto,
        @PathVariable(value = "perfumeId") perfumeId: Long,
        @RequestParam(value = "cursor", required = false)cursor: Long? = null
    ): SeehyangResponse<List<StoryDto>>{

        val storyListDto = storyService.getStoriesByPerfume(perfumeId, userDto, cursor)

        return SeehyangResponse(storyListDto)
    }

    @PostMapping("/story")
    fun createStory(
        @RequestBody createRequest: StoryCreateRequest,
        @ApiIgnore userDto: UserDto,
    ) : SeehyangResponse<StoryCreateResponse> {

        val storyDto = storyService.createStory(userDto, createRequest)

        return SeehyangResponse(StoryCreateResponse(storyDto))
    }



    @PostMapping("/story/{storyId}/like")
    fun likeStory(
        @ApiIgnore userDto: UserDto,
        @PathVariable("storyId") storyId : Long,
    ): SeehyangResponse<Map<String, Boolean>> {

        val isLike = storyService.likeStory(userDto, storyId)

        return SeehyangResponse(mutableMapOf(Pair("isLike", isLike)))
    }

    @DeleteMapping("/story/{id}")
    fun deleteStory(
        @ApiIgnore userDto: UserDto,
        @PathVariable id: Long
    ): SeehyangResponse<Map<String, Long>>{

        val deletedId = storyService.deleteStory(id,userDto)

        return SeehyangResponse(mutableMapOf(Pair("id", deletedId)))
    }

}