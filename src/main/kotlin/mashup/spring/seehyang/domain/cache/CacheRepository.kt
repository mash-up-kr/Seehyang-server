package mashup.spring.seehyang.domain.cache

import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.exception.InternalServerException
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class CacheRepository(
    val cacheManager: CacheManager
) {

    fun getCache(cacheType: CacheType, key: String): Any?
        = when (cacheType) {
            CacheType.WEEKLY_RANKING -> {
                val cache = cacheManager.getCache(CacheType.WEEKLY_RANKING.cacheName)?: throw InternalServerException(SeehyangStatus.INTERNAL_SERVER_ERROR)
                val result = cache.get(key) ?.get()
                result
            }
            CacheType.HOT_STORY -> {
                val cache = cacheManager.getCache(CacheType.HOT_STORY.cacheName) ?: throw InternalServerException(SeehyangStatus.INTERNAL_SERVER_ERROR)
                val result =  cache.get(key)?.get()
                result
            }
        }


    fun save(cacheType: CacheType, key: String, objects: Any?)
        = when (cacheType) {
            CacheType.WEEKLY_RANKING -> {
                val cache = cacheManager.getCache(CacheType.WEEKLY_RANKING.cacheName) ?: throw InternalServerException(SeehyangStatus.INTERNAL_SERVER_ERROR)
                cache.put(key, objects)
            }
            CacheType.HOT_STORY -> {
                val cache = cacheManager.getCache(CacheType.HOT_STORY.cacheName) ?: throw InternalServerException(SeehyangStatus.INTERNAL_SERVER_ERROR)
                cache.put(key, objects)
            }
        }

}
