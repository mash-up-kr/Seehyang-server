package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryCreateResponse
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.service.StoryService
import mashup.spring.seehyang.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.HttpServletRequest

@ApiV1
class StoryApiController(
    private val userService: UserService,
    private val storyService: StoryService
) {

    @GetMapping("/story/{id}")
    fun getStory(
        @PathVariable id : Long
    ): SeehyangResponse<StoryDto> {
        val story = storyService.getStory(id)
        return SeehyangResponse(StoryDto(story))
    }

    @Authenticated
    @PostMapping("/story")
    fun createStory(
        createRequest: StoryCreateRequest,
        request: HttpServletRequest
    ) : SeehyangResponse<StoryCreateResponse> {
        val user = userService.getUser(request)

        val story = storyService.create(user, createRequest)

        return SeehyangResponse(StoryCreateResponse(story))
    }

}