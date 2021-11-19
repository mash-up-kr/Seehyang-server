package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.dto.home.TodaySeehyangDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.domain.cache.CacheRepository
import mashup.spring.seehyang.domain.cache.CacheType
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.exception.InternalServerException
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.springframework.data.domain.PageRequest
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
    private val STEADY_PAGE_SIZE = 6
    private val TODAY_PERFUME_BASELINE = 5
    /**
     * 1. 스토리가 5개 이상 올라온 향수를 찾는다.
     * 2. 찾은 향수 중 랜덤으로 하나를 선택 한다.
     * 3. 선택한 향수에서 좋아요 개수 상위 10개를 가져온다.
     * */
    fun todaySeehyang() : TodaySeehyangDto{
        val perfumeIdList = perfumeRepository.findByStoryLengthGreaterThan(TODAY_PERFUME_BASELINE).map { it.id }
        val randIndex = Random.nextInt(perfumeIdList.size)
        val randId = perfumeIdList[randIndex]
        val stories = storyRepository.findTop10ByPerfumeIdOrderByLikeCountDesc(randId)
        return TodaySeehyangDto(stories[0].perfume,stories)

    }

    fun hotStory() : List<StoryDto> {
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
        return storyRepository.findByIds(storyIds).map { StoryDto(it) }
    }

    fun weeklyRanking(): List<PerfumeDto>{
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

        return perfumeRepository.findByIds(perfumeIds).map { PerfumeDto(it) }

    }

    fun getSteadyPerfumes(idCursor: Long?, likeCursor: Int?): List<PerfumeDto>{

        return if(idCursor == null && likeCursor == null) {
            perfumeRepository.findTop6ByOrderByLikeCountDescIdDesc().map { PerfumeDto(it) }
        }else if(idCursor == null && likeCursor != null){
            throw BadRequestException(SeehyangStatus.INVALID_CURSOR_PARAMETER)
        }else if(idCursor != null && likeCursor == null){
            perfumeRepository.findSteadyPerfume(idCursor, PageRequest.ofSize(STEADY_PAGE_SIZE)).map { PerfumeDto(it) }
        }
        else {
            perfumeRepository.findSteadyPerfume(idCursor!!, likeCursor!!, PageRequest.ofSize(STEADY_PAGE_SIZE)).map { PerfumeDto(it) }
        }
    }
}