package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.domain.entity.community.StoryLike
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface StoryLikeRepository : JpaRepository<StoryLike, Long> {

    @Query("select count(l) " +
            "from StoryLike l " +
                   "join  l.story s " +
                   "where s.id = :storyId")
    fun countByStoryId(@Param("storyId") storyId: Long) : Long

    fun findByUserAndStory(user: User, story: Story) : Optional<StoryLike>

    @Query(nativeQuery = true,
           value =  "select * " +
                   "from like_table l join story s on s.id = l.story_id " +
                   "where l.created_at > :from and l.created_at < :to " +
                   "group by s.id " +
                   "order by count(s.id) desc " +
                   "limit :limit")
    fun findStoryIdByRecentLike(@Param("from")from: LocalDateTime,
                                @Param("to") to: LocalDateTime,
                                @Param("limit") limit: Int): List<Long>
}