package mashup.spring.perfumate.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    @GetMapping("/health")
    fun health() : String {
        return "Perfumate!"
    }
}