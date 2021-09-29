package mashup.spring.seehyang

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class SeehyangApplication

fun main(args: Array<String>) {
	runApplication<SeehyangApplication>(*args)
}
