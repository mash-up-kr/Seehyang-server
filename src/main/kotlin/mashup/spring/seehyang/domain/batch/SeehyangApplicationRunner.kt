package mashup.spring.seehyang.domain.batch

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
        val mondayOfThisWeek = LocalDateTime.now().with(DayOfWeek.MONDAY)
        scheduler.saveWeeklyRanking(from = mondayOfThisWeek
                                            .minusWeeks(1),
                                    to = LocalDateTime.now()
                                            .with(DayOfWeek.MONDAY))

        val oCLockOfThisHour = LocalDateTime.now().minusMinutes(LocalDateTime.now().minute.toLong())
        scheduler.saveHotStory(oCLockOfThisHour.minusHours(1), oCLockOfThisHour)
    }


}