package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.domain.entity.community.Story
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoryRepository : JpaRepository<Story, Long>{
}