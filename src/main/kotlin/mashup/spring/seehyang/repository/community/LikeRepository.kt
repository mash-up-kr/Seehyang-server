package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.domain.entity.community.Like
import mashup.spring.seehyang.domain.entity.community.Story
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository : JpaRepository<Like, Long> {

    @Query("select count(l) " +
                   "from Like l " +
                   "join l.story s " +
                   "where s.id = :storyId")
    fun countByStoryId(@Param("storyId") storyId: Long) : Long
}