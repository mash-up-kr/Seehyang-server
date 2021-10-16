package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.domain.entity.community.StoryLike
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StoryLikeRepository : JpaRepository<StoryLike, Long> {

    @Query("select count(l) " +
            "from StoryLike l " +
                   "join l.story s " +
                   "where s.id = :storyId")
    fun countByStoryId(@Param("storyId") storyId: Long) : Long

    fun findByUserAndStory(user: User, story: Story) : Optional<StoryLike>
}