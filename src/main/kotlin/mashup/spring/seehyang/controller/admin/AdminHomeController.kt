package mashup.spring.seehyang.controller.admin

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.ui.Model;

@AdminController
class AdminHomeController {
    @GetMapping("/home")
    fun home(model: Model): String {
        val entities = mutableListOf<String>("Perfume", "Brand", "Note", "Accord", "Story", "Tag", "User")
        model.addAttribute("entities", entities)
        return "home"
    }
}