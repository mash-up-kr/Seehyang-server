package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.domain.entity.community.StoryTag
import mashup.spring.seehyang.domain.entity.community.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository: JpaRepository<Tag, Long> {

    fun existsByContents(contents: String) : Boolean

    fun findByContents(contents: String) : Tag
}