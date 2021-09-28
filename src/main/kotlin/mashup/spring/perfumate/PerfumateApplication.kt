package mashup.spring.perfumate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@ConfigurationPropertiesScan
@EnableJpaAuditing
@SpringBootApplication
class PerfumateApplication

fun main(args: Array<String>) {
	runApplication<PerfumateApplication>(*args)
}
