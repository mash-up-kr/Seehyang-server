package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.community.StoryTag
import mashup.spring.seehyang.domain.entity.community.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface StoryTagRepository: JpaRepository<StoryTag, Long> {

    @Query("select st from StoryTag st " +
                   "join fetch st.tag " +
                   "join fetch st.story " +
           "where st.story.id = :storyId")
    fun findByStoryId(@Param("storyId") storyId: Long): ArrayList<StoryTag>
}