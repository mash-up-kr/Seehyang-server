package mashup.spring.seehyang.domain

import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.cache.CacheRepository
import mashup.spring.seehyang.domain.cache.CacheType
import mashup.spring.seehyang.domain.enums.Domain
import mashup.spring.seehyang.exception.InternalServerException

@Domain
class CacheDomain(
    val cacheRepository: CacheRepository
) {
    fun getHotStoryList(): List<Long> {
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

        return storyIds
    }

    fun getWeeklyRankingStoryIds():List<Long>{
        val cacheType = CacheType.WEEKLY_RANKING
        val perfumeIds = mutableListOf<Long>()

        //TODO : Cache Domain 으로 빼기
        for(i in 1..cacheType.maximumSize!!){
            val id = cacheRepository.getCache(cacheType, i.toString()) ?: break
            if(id !is Long){
                throw InternalServerException(SeehyangStatus.INTERNAL_SERVER_ERROR)
            }else{
                perfumeIds.add(id)
            }
        }

        return perfumeIds
    }


}