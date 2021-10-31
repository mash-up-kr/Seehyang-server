package mashup.spring.seehyang.cache

import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class CacheRepository(
    val cacheManager: CacheManager
) {

    fun <T> getCache(cacheType: CacheType, key: String, type: Class<T>): T?
        = when (cacheType) {
            CacheType.WEEKLY_RANKING -> {
                val cache = cacheManager.getCache(CacheType.WEEKLY_RANKING.cacheName) ?: throw RuntimeException("Weekly Ranking Cache Not Found")
                cache.get(key, type)
            }
            CacheType.HOT_STORY -> {
                val cache = cacheManager.getCache(CacheType.HOT_STORY.cacheName) ?: throw RuntimeException("Hot Story Cache Not Found")
                cache.get(key, type)
            }
        }


    fun save(cacheType: CacheType, key: String, objects: Any?)
        = when (cacheType) {
            CacheType.WEEKLY_RANKING -> {
                val cache = cacheManager.getCache(CacheType.WEEKLY_RANKING.cacheName) ?: throw RuntimeException("Weekly Ranking Cache Not Found")
                cache.put(key, objects)
            }
            CacheType.HOT_STORY -> {
                val cache = cacheManager.getCache(CacheType.HOT_STORY.cacheName) ?: throw RuntimeException("Hot Cache Not Found")
                cache.put(key, objects)
            }
        }

}
