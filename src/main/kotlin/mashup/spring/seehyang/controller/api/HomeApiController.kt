package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.home.HotStoryDto
import mashup.spring.seehyang.controller.api.dto.home.TodaySeehyangDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.service.HomeService
import org.springframework.web.bind.annotation.GetMapping

@ApiV1
class HomeApiController(
    val homeService: HomeService
) {

    @GetMapping("/today")
    fun today() : SeehyangResponse<TodaySeehyangDto> {
        val stories = homeService.todaySeehyang()
        return SeehyangResponse(TodaySeehyangDto(stories[0].perfume, stories))
    }

    @GetMapping("/hot-story")
    fun hotStory(): SeehyangResponse<HotStoryDto> {
        val stories = homeService.hotStory()
        return SeehyangResponse(HotStoryDto(stories))
    }
}