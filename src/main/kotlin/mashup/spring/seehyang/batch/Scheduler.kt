package mashup.spring.seehyang.batch

import mashup.spring.seehyang.cache.CacheRepository
import mashup.spring.seehyang.cache.CacheType
import mashup.spring.seehyang.repository.community.StoryLikeRepository
import mashup.spring.seehyang.repository.perfume.PerfumeLikeRepository
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class Scheduler(
    val perfumeLikeRepository: PerfumeLikeRepository,
    val storyLikeRepository: StoryLikeRepository,
    val cacheRepository: CacheRepository
) {

    @Scheduled(cron = "0 0 0/1 * *")
    fun updateHotStory(): Unit{
        val storyIds = storyLikeRepository
            .findStoryIdByRecentLike(from = LocalDateTime.now().minusDays(1),
                                     to = LocalDateTime.now(),
                                     Pageable.ofSize(10))
        for(i in 1..10){
            cacheRepository.save(CacheType.HOT_STORY, i.toString(), storyIds[i])
        }

    }

    @Scheduled(cron = "0 0 0 * * MON")
    fun updateWeeklyRanking(): Unit{
        val perfumeIds = perfumeLikeRepository
            .findPerfumeIdByRecentLike(from = LocalDateTime.now().minusWeeks(1),
                                       to = LocalDateTime.now(),
                                       Pageable.ofSize(10))
        for(i in 1..10){
            cacheRepository.save(CacheType.WEEKLY_RANKING, i.toString(), perfumeIds[i])
        }

    }

}