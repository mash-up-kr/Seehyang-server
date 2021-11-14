package mashup.spring.seehyang.controller.admin

import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.domain.entity.perfume.Brand
import mashup.spring.seehyang.domain.entity.perfume.Gender
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.perfume.PerfumeType
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.service.PerfumeService
import org.springframework.data.domain.PageRequest
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import javax.annotation.PostConstruct

@AdminController
class AdminPerfumeController(
    val perfumeService: PerfumeService,
    val perfumeRepository: PerfumeRepository
) {

    @GetMapping("/entity/Perfume")
    fun adminPerfume(
        model : Model,
        @RequestParam(value = "page", defaultValue = "1") page : Int
    ): String {
        val pageRequest = PageRequest.of(page - 1, 30)
        val items = perfumeRepository.findAll(pageRequest).content
        val count = perfumeRepository.count()

        val maxPage = if (count%30L==0L) count/30 else count/30 + 1

        model.addAttribute("items", items)
        model.addAttribute("currentPage", page)
        model.addAttribute("maxPage", maxPage)
        return "perfume/perfumeList"
    }

    @GetMapping("/entity/Perfume/{id}")
    fun adminPerfumeDetail(
        model: Model,
        @PathVariable id: Long,
        @RequestParam(value = "page", defaultValue = "1") page : Int
    ) : String{
        val perfume = perfumeService.get(User.empty(), id)
        model.addAttribute("item", perfume)
        model.addAttribute("prevPage", page)
        return "perfume/perfumeDetail"
    }
}