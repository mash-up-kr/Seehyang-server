package mashup.spring.seehyang.batch

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDateTime

@Component
class SeehyangApplicationRunner(
    val scheduler: Scheduler
): ApplicationRunner {


    override fun run(args: ApplicationArguments?) {
        initCache()
    }

    private fun initCache(){
        scheduler.saveWeeklyRanking(LocalDateTime.now().with(DayOfWeek.MONDAY), LocalDateTime.now())
        scheduler.saveHotStory(LocalDateTime.now().minusMinutes(LocalDateTime.now().minute.toLong()), LocalDateTime.now())
    }


}