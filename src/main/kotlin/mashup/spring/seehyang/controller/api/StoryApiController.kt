package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryCreateResponse
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.service.StoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@ApiV1
class StoryApiController(
    private val storyService: StoryService,
    private val userRepository: UserRepository // 테스트를 위한 임시 작업.
) {

    @PostMapping("/story")
    fun createStory(createRequest: StoryCreateRequest) : SeehyangResponse<StoryCreateResponse> {
        // Todo: User Service 호출로 변경
        val user = userRepository.findById(1L).get()

        val story = storyService.create(user, createRequest)

        return SeehyangResponse(StoryCreateResponse(StoryDto(story)))
    }

}