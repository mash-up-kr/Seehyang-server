package mashup.spring.seehyang.controller.admin

import mashup.spring.seehyang.repository.perfume.BrandRepository
import org.springframework.data.domain.PageRequest
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@AdminController
class AdminBrandController(
    val brandRepository: BrandRepository
) {

    @GetMapping("/entity/Brand")
    fun adminPerfume(
        model : Model,
        @RequestParam(value = "page", defaultValue = "1") page : Int
    ): String {
        val pageRequest = PageRequest.of(page - 1, 20)
        val items = brandRepository.findAll(pageRequest).content
        val count = brandRepository.count()

        val maxPage = if (count%20L==0L) count/20 else count/20 + 1

        model.addAttribute("items", items)
        model.addAttribute("currentPage", page)
        model.addAttribute("maxPage", maxPage)
        return "perfume/brandList"
    }

    @GetMapping("/entity/Brand/{id}")
    fun adminPerfumeDetail(
        model: Model,
        @PathVariable id: Long,
        @RequestParam(value = "page", defaultValue = "1") page : Int
    ) : String{
        val brand = brandRepository.findById(id).get()
        model.addAttribute("item", brand)
        model.addAttribute("prevPage", page)
        return "perfume/brandDetail"
    }
}