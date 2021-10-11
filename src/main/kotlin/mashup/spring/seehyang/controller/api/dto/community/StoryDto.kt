package mashup.spring.seehyang.controller.api.dto.community

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
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

@JsonInclude(JsonInclude.Include.NON_NULL)
data class StoryDetailDto(
    val id: Long,
    val userNickname: String,
    val userProfileUrl: String,
    val commentCount: Int? = null,
    val likeCount: Int,
    val perfumeName: String? = null,
    val tags: List<String>? = null,
    val storyImageUrl: String
)