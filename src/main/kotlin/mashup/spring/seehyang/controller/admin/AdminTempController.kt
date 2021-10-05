package mashup.spring.seehyang.controller.admin

import org.springframework.data.domain.PageRequest
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminTempController {

    @GetMapping("/tteokbokki/entity/{etc}")
    fun adminPerfume(
        @RequestParam(value = "page", defaultValue = "1") page : Int
    ): String {
        return "여긴 아직 구현 안됐지롱"
    }
}