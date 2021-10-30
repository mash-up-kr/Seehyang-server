package mashup.spring.seehyang.config

import com.github.benmanes.caffeine.cache.Caffeine
import mashup.spring.seehyang.cache.CacheType
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CacheConfig {

    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = SimpleCacheManager()

        val cacheTypes = CacheType.values()
        val cacheList = mutableListOf<Cache>()

        for(type in cacheTypes){
            when(type){
                CacheType.WEEKLY_RANKING -> {

                    val cache = CaffeineCache(
                        type.cacheName,
                        Caffeine.newBuilder()
                            .initialCapacity(type.maximumSize!!.toInt())
                            .maximumSize(type.maximumSize!!)
                            .build()
                    )
                    cacheList.add(cache)
                }
            }
        }

        cacheManager.setCaches(cacheList)
        return cacheManager
    }
}