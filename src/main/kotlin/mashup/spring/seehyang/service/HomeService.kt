package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.dto.home.TodaySeehyangDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.domain.cache.CacheType
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.CacheDomain
import mashup.spring.seehyang.domain.PerfumeDomain
import mashup.spring.seehyang.domain.StoryDomain
import mashup.spring.seehyang.domain.StorySortType
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.InternalServerException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class HomeService(
    val storyDomain: StoryDomain,
    val perfumeDomain: PerfumeDomain,
    val cacheDomain: CacheDomain
) {


    private val TODAY_PERFUME_BASELINE = 5

    /**
     * 1. 스토리가 5개 이상 올라온 향수를 찾는다.
     * 2. 찾은 향수 중 랜덤으로 하나를 선택 한다.
     * 3. 선택한 향수에서 좋아요 개수 상위 10개를 가져온다.
     * */
    fun todaySeehyang(): TodaySeehyangDto {

        val perfumeIdList = perfumeDomain.getPerfumesWithStoriesMoreThan(TODAY_PERFUME_BASELINE).map { it.id!! }
        val randId = perfumeIdList.random()
        val stories = storyDomain.getStoriesByPerfume(randId, User.empty(), pageSize = 10, StorySortType.LIKE, cursor = null)

        return TodaySeehyangDto(stories[0].perfume, stories)

    }

    fun hotStory(): List<StoryDto> {


        val storyIds = cacheDomain.getHotStoryList()

        return storyDomain.getStoriesByIds(storyIds, User.empty()).map { StoryDto(it) }
    }

    fun weeklyRanking(): List<PerfumeDto> {


        val perfumeIds = cacheDomain.getWeeklyRankingStoryIds()

        return perfumeDomain.getPerfumes(perfumeIds).map { PerfumeDto(it) }

    }

    fun getSteadyPerfumes(idCursor: Long?, likeCursor: Int?): List<PerfumeDto> {

        return perfumeDomain.getSteadyPerfumes(idCursor, likeCursor).map { PerfumeDto(it) }
    }
}