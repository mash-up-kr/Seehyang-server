package mashup.spring.seehyang.service

import mashup.spring.seehyang.domain.cache.CacheRepository
import mashup.spring.seehyang.domain.cache.CacheType
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.exception.InternalServerException
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Transactional
@Service
class HomeService(
    val storyRepository: StoryRepository,
    val perfumeRepository: PerfumeRepository,
    val cacheRepository: CacheRepository
) {
    private val STEADY_PAGE_SIZE = 6;
    /**
     * 1. 스토리가 5개 이상 올라온 향수를 찾는다.
     * 2. 찾은 향수 중 랜덤으로 하나를 선택 한다.
     * 3. 선택한 향수에서 좋아요 개수 상위 10개를 가져온다.
     * */
    fun todaySeehyang() : List<Story>{
        val perfumeIdList = perfumeRepository.findByStoryLengthGreaterThan().map { it.id }
        val randIndex = Random.nextInt(perfumeIdList.size)
        val randId = perfumeIdList[randIndex]
        val todaySeehyangStories = storyRepository.findTop10ByPerfumeIdOrderByLikeCountDesc(randId)
        return todaySeehyangStories
    }

    fun hotStory() : List<Story> {
        val cacheType = CacheType.HOT_STORY

        val storyIds = mutableListOf<Long>()

        for(i in 1..cacheType.maximumSize!!){
            val id = cacheRepository.getCache(cacheType, i.toString()) ?: break
            if(id !is Long){
                throw InternalServerException(SeehyangStatus.INTERNAL_SERVER_ERROR)
            }else{
                storyIds.add(id)
            }
        }
        return storyRepository.findByIds(storyIds)
    }

    fun weeklyRanking(): List<Perfume>{
        val cacheType = CacheType.WEEKLY_RANKING

        val perfumeIds = mutableListOf<Long>()

        for(i in 1..cacheType.maximumSize!!){
            val id = cacheRepository.getCache(cacheType, i.toString()) ?: break
            if(id !is Long){
                throw InternalServerException(SeehyangStatus.INTERNAL_SERVER_ERROR)
            }else{
                perfumeIds.add(id)
            }
        }

        return perfumeRepository.findByIds(perfumeIds)

    }

    fun getSteadyPerfumes(idCursor: Long?, likeCursor: Int?): List<Perfume>{

        return if(idCursor == null && likeCursor == null) {
            perfumeRepository.findTop6ByOrderByLikeCountDescIdDesc()
        }else if(idCursor == null && likeCursor != null){
            throw BadRequestException(SeehyangStatus.INVALID_CURSOR_PARAMETER)
        }else if(idCursor != null && likeCursor == null){
            perfumeRepository.findSteadyPerfume(idCursor, STEADY_PAGE_SIZE)
        }
        else {
            perfumeRepository.findSteadyPerfume(idCursor!!, likeCursor!!, STEADY_PAGE_SIZE)
        }
    }
}