package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.dto.home.TodaySeehyangDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.service.HomeService
import mashup.spring.seehyang.service.auth.UserId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class HomeApiController(
    val homeService: HomeService
) {

    @GetMapping("/home/today")
    fun today(@ApiIgnore userId: UserId?) : SeehyangResponse<TodaySeehyangDto> = SeehyangResponse(homeService.todaySeehyang())

    @GetMapping("/home/hot-story")
    fun hotStory(): SeehyangResponse<List<StoryDto>> = SeehyangResponse(homeService.hotStory())

    @GetMapping("/home/weekly-ranking")
    fun weeklyRanking(): SeehyangResponse<List<PerfumeDto>> = SeehyangResponse(homeService.weeklyRanking())

    @GetMapping("/home/steady-perfume")
    fun steadyPerfume(
        @RequestParam(value = "idCursor", required = false) idCursor :Long? = null,
        @RequestParam(value = "likeCursor", required = false) likeCursor: Int? = null
    ): SeehyangResponse<List<PerfumeDto>> = SeehyangResponse(homeService.getSteadyPerfumes(idCursor, likeCursor))

}