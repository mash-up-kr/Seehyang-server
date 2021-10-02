package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryCreateResponse
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.service.StoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@ApiV1
class StoryApiController(
    private val storyService: StoryService
) {

    @PostMapping("/story")
    fun createStory(createRequest: StoryCreateRequest) : StoryCreateResponse{
        // Todo: User Service 호출

        // Mock up Return
        return StoryCreateResponse(StoryDto())
    }

}