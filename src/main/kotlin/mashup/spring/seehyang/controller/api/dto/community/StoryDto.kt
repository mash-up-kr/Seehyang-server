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
    val userId : Long = story.user.id!!,
    val userNickname: String = story.user.nickname!!,
    val userProfileImageUrl: String? = story.user.profileImage?.url,
    val commentCount: Int = story.commentCount,
    val likeCount: Int = story.likeCount,
    val tags: List<TagDto> = story.storyTags.map { TagDto(it.tag) }
)