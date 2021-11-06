package mashup.spring.seehyang.controller.api

import io.swagger.annotations.ApiParam
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.dto.home.TodaySeehyangDto
import mashup.spring.seehyang.controller.api.dto.home.WeeklyDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.service.HomeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@ApiV1
class HomeApiController(
    val homeService: HomeService
) {

    @GetMapping("/home/today")
    fun today() : SeehyangResponse<TodaySeehyangDto> {
        val stories = homeService.todaySeehyang()
        return SeehyangResponse(TodaySeehyangDto(stories[0].perfume, stories))
    }

    @GetMapping("/home/hot-story")
    fun hotStory(): SeehyangResponse<List<StoryDto>> {
        val stories = homeService.hotStory().map{StoryDto(it)}
        return SeehyangResponse(stories)
    }

    @GetMapping("/home/weekly-ranking")
    fun weeklyRanking(): SeehyangResponse<List<PerfumeDto>> {
        val perfumes = homeService.weeklyRanking().map{PerfumeDto(it)}
        return SeehyangResponse(perfumes)
    }

    @GetMapping("/home/steady-perfume")
    fun steadyPerfume(
        @RequestParam(value = "idCursor", required = false) idCursor :Long? = null,
        @RequestParam(value = "likeCursor", required = false) likeCursor: Int? = null
    ): SeehyangResponse<List<PerfumeDto>>{
        val perfumes = homeService.getSteadyPerfumes(idCursor, likeCursor).map{PerfumeDto(it)}
        return SeehyangResponse(perfumes)
    }
}