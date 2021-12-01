package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.domain.entity.community.StoryLike
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
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

    @EntityGraph(attributePaths = ["story","user"])
    @Query("select st " +
                   "from StoryLike st " +
                   "where st.user = :user and st.story = :story")
    fun findByUserAndStory(@Param("user") user: User, @Param("story") story: Story) : Optional<StoryLike>

    @Query("select s " +
                   "from StoryLike l join l.story s " +
                   "where l.createdAt >:from and l.createdAt <:to and s.isOnlyMe = false " +
                   "group by s " +
                   "order by count(l) desc")
    fun findStoryIdByRecentLike(@Param("from")from: LocalDateTime,
                                @Param("to") to: LocalDateTime, pageable: Pageable): List<Story>

}