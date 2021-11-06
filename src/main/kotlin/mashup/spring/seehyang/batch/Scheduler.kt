package mashup.spring.seehyang.batch

import mashup.spring.seehyang.cache.CacheRepository
import mashup.spring.seehyang.cache.CacheType
import mashup.spring.seehyang.repository.community.StoryLikeRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.perfume.PerfumeLikeRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class Scheduler(
    val perfumeLikeRepository: PerfumeLikeRepository,
    val storyLikeRepository: StoryLikeRepository,
    val storyRepository: StoryRepository,
    val perfumeRepository: PerfumeRepository,
    val cacheRepository: CacheRepository
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

        val hotStoryIds = storyLikeRepository
            .findStoryIdByRecentLike(
                from = from,
                to = to,
                Pageable.ofSize(HOT_STORY_SIZE)
            )

        if(hotStoryIds.size < HOT_STORY_SIZE){

            val defaultStories = storyRepository.findTop10ByOrderByLikeCountDesc().map { it.id!! }.toList()
            for (i in defaultStories.indices) {
                cacheRepository.save(CacheType.HOT_STORY, (i+1).toString(), defaultStories[i])
            }

        }else {
            for (i in 0 until HOT_STORY_SIZE) {
                cacheRepository.save(CacheType.HOT_STORY, (i+1).toString(), hotStoryIds[i])
            }
        }
    }



    fun saveWeeklyRanking(from: LocalDateTime, to: LocalDateTime) {

        val WEEKLY_RANKING_SIZE = CacheType.WEEKLY_RANKING.maximumSize!!.toInt()

        val perfumeIds = perfumeLikeRepository
            .findPerfumeIdByRecentLike(
                from = from,
                to = to,
                Pageable.ofSize(WEEKLY_RANKING_SIZE)
            )

        if(perfumeIds.size < WEEKLY_RANKING_SIZE){
            val defaultPerfumes = perfumeRepository.findTop10ByOrderByLikeCountDesc().map { it.id!! }
            for (i in defaultPerfumes.indices) {
                cacheRepository.save(CacheType.WEEKLY_RANKING, (i+1).toString(), defaultPerfumes[i])
            }
        }else {
            for (i in 0 until WEEKLY_RANKING_SIZE) {
                cacheRepository.save(CacheType.WEEKLY_RANKING, (i+1).toString(), perfumeIds[i])
            }
        }
    }

}