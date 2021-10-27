package mashup.spring.seehyang.service

import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Transactional
@Service
class HomeService(
    val storyRepository: StoryRepository,
    val perfumeRepository: PerfumeRepository
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
        return storyRepository.findTop10By()
    }

    fun weeklyRanking(): List<Perfume>{
        return perfumeRepository.findTop10ByOrderByLikeCountDesc()
    }

    fun getSteadyPerfumes(idCursor: Long?, likeCursor: Int?): List<Perfume>{

        return if(idCursor == null || likeCursor == null) {
            perfumeRepository.findTop6ByOrderByLikeCountDescIdDesc()
        }else{
            perfumeRepository.findSteadyPerfume(idCursor, likeCursor, STEADY_PAGE_SIZE)
        }
    }
}