package mashup.spring.seehyang.controller.api.dto.community

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.domain.entity.community.Story

data class StoryDto(
    @JsonIgnore
    val story: Story,
    val id: Long = story.id!!,
    val image: String = story.image.url,
    val perfumeImageUrl : String = story.perfume.thumbnailUrl,
    val tags: MutableList<TagDto> = mutableListOf()
)