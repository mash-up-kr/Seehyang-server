package mashup.spring.seehyang.domain.batch

import mashup.spring.seehyang.domain.CacheDomain
import mashup.spring.seehyang.domain.PerfumeDomain
import mashup.spring.seehyang.domain.StoryDomain
import mashup.spring.seehyang.domain.cache.CacheType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class Scheduler(
    val perfumeDomain: PerfumeDomain,
    val storyDomain: StoryDomain,
    val cacheDomain: CacheDomain
) {

    @Scheduled(cron = "0 0 0/1 * *")
    fun updateHotStory(): Unit{
        saveHotStory(LocalDateTime.now().minusHours(1), LocalDateTime.now())
    }

    @Scheduled(cron = "0 0 0 * * MON")
    fun updateWeeklyRanking(): Unit{
        saveWeeklyRanking(from = LocalDateTime.now().minusWeeks(1),
                          to = LocalDateTime.now())

    }

    fun saveHotStory(from: LocalDateTime, to: LocalDateTime) {

        val HOT_STORY_SIZE = CacheType.HOT_STORY.maximumSize!!.toInt()

        val hotStoryIds = storyDomain
            .getStoryIdByRecentLike(
                from = from,
                to = to,
                HOT_STORY_SIZE
            )

        if(hotStoryIds.size < HOT_STORY_SIZE){

            val defaultStories = storyDomain.getTopTenOrderByLike().map { it.id!! }.toList()
            for (i in defaultStories.indices) {
                cacheDomain.saveEntity(CacheType.HOT_STORY, (i+1).toString(), defaultStories[i])
            }

        }else {
            for (i in 0 until HOT_STORY_SIZE) {
                cacheDomain.saveEntity(CacheType.HOT_STORY, (i+1).toString(), hotStoryIds[i])
            }
        }
    }



    fun saveWeeklyRanking(from: LocalDateTime, to: LocalDateTime) {

        val WEEKLY_RANKING_SIZE = CacheType.WEEKLY_RANKING.maximumSize!!.toInt()

        val perfumeIds = perfumeDomain.getRecentLikedPerfumes(
                from = from,
                to = to,
                size = WEEKLY_RANKING_SIZE
            )

        if(perfumeIds.size < WEEKLY_RANKING_SIZE){
            val defaultPerfumes = perfumeDomain.getTopLikedPerfumeIds(size = 10)
            for (i in defaultPerfumes.indices) {
                cacheDomain.saveEntity(CacheType.WEEKLY_RANKING, (i+1).toString(), defaultPerfumes[i])
            }
        }else {
            for (i in 0 until WEEKLY_RANKING_SIZE) {
                cacheDomain.saveEntity(CacheType.WEEKLY_RANKING, (i+1).toString(), perfumeIds[i])
            }
        }
    }

}