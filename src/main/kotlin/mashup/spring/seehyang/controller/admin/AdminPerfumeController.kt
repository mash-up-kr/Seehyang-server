package mashup.spring.seehyang.controller.admin

import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.domain.entity.perfume.Brand
import mashup.spring.seehyang.domain.entity.perfume.Gender
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.perfume.PerfumeType
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.service.PerfumeService
import org.springframework.data.domain.PageRequest
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.annotation.PostConstruct

@AdminController
class AdminPerfumeController(
    val perfumeService: PerfumeService,
    val brandRepository: BrandRepository,
    val perfumeRepository: PerfumeRepository
) {

    @GetMapping("/entity/Perfume")
    fun adminPerfume(
        model : Model,
        @RequestParam(value = "page", defaultValue = "1") page : Int
    ): String {
        val pageRequest = PageRequest.of(page - 1, 20)
        val items = perfumeRepository.findAll(pageRequest).content
        val count = perfumeRepository.count()

        val maxPage = if (count%20L==0L) count/20 else count/20 + 1

        model.addAttribute("items", items)
        model.addAttribute("currentPage", page)
        model.addAttribute("maxPage", maxPage)
        return "perfume/perfumeList"
    }

    @PostConstruct
    fun warm() {
//        val brand = brandRepository.save(Brand(name = "chanel", koreanName = "샤넬"))
//        perfumeRepository.save(Perfume(brand=brand, name = "perf", koreanName = "펖", type = "EAU_DE", gender=Gender.BOTH, thumbnailUrl = ""))
//        perfumeRepository.save(Perfume(brand=brand, name = "perf2", koreanName = "펖", type = "EAU_DE, gender=Gender.BOTH, thumbnailUrl = ""))
//        perfumeRepository.save(Perfume(brand=brand, name = "perf3", koreanName = "펖", type = PerfumeType.EAU_DE, gender=Gender.BOTH, thumbnailUrl = ""))
//        perfumeRepository.save(Perfume(brand=brand, name = "perf4", koreanName = "펖", type = PerfumeType.EAU_DE, gender=Gender.BOTH, thumbnailUrl = ""))
    }
}