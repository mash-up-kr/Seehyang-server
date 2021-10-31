package mashup.spring.seehyang.cache

import java.time.Duration


enum class CacheType(
    val cacheName: String,
    val maximumSize: Long?,
    val expAfter: Duration?
) {

    WEEKLY_RANKING("weeklyRanking", 10,null),
    HOT_STORY("hotStory", 10,null)
}