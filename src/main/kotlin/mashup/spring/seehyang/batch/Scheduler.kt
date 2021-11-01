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

            val defaultStories = storyRepository.findTop10ByOrderByLikeCountDesc().map { it.id!! }.toMutableList()

            if(defaultStories.size < HOT_STORY_SIZE){
                if(defaultStories.isEmpty()){
                    //Not Found Exception 발생
                    return
                }else{

                    val missingStoryCount = HOT_STORY_SIZE - defaultStories.size
                    // 현재 스토리 4개 있고 -> 0 1 2 3
                    // 6개를 채워야 한다 -> 0 1 2 3 0 1  -> 6개 채워주자.
                    // i : 0 until missingCount -> 0 1 2 3 4 5
                    // addIdx: i%defaultStories.size -> 0 1 2 3 0 1
                    val defaultStorySize = defaultStories.size
                    for(i in 0 until missingStoryCount){
                        val addIdx = i%defaultStorySize
                        defaultStories.add(defaultStories[addIdx])
                    }
                    for (i in 0 until HOT_STORY_SIZE) {
                        cacheRepository.save(CacheType.HOT_STORY, (i+1).toString(), defaultStories[i])
                    }

                }
            }else{
                for (i in 0 until HOT_STORY_SIZE) {
                    cacheRepository.save(CacheType.HOT_STORY, (i+1).toString(), defaultStories[i])
                }
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
            for (i in 0 until WEEKLY_RANKING_SIZE) {
                cacheRepository.save(CacheType.WEEKLY_RANKING, (i+1).toString(), defaultPerfumes[i])
            }
        }else {
            for (i in 0 until WEEKLY_RANKING_SIZE) {
                cacheRepository.save(CacheType.WEEKLY_RANKING, (i+1).toString(), perfumeIds[i])
            }
        }
    }

}