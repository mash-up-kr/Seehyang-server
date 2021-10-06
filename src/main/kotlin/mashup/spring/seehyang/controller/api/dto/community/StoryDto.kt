package mashup.spring.seehyang.controller.api.dto.community

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.community.Tag
import java.time.LocalDateTime

data class StoryDto(
    @JsonIgnore
    val story: Story,
    val id: Long = story.id!!,
    val image: String = story.image.url,
    val perfumeImageUrl: String = story.perfume.thumbnailUrl,
    val tags: List<TagDto> = story.storyTags.map { TagDto(it.tag) }
)

data class StoryDetailDto(
    val id: Long,
    val userNickname: String,
    val userProfileUrl: String,
    val commentCount: Int,
    val likeCount: Int,
    val tags: List<String>,
    val storyImageUrl: String
)