package mashup.spring.seehyang.controller.api.dto.home

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.controller.api.dto.addBucketUrl
import mashup.spring.seehyang.controller.api.dto.community.TagDto
import mashup.spring.seehyang.domain.entity.community.Story
import java.time.LocalDateTime

data class HotStoryDto (
    @JsonIgnore
    val story: Story,
    val id: Long = story.id!!,
    val imageUrl: String = addBucketUrl(story.image.url),
    val perfumeId: Long = story.perfume.id!!,
    val perfumeName: String = story.perfume.name,
    val perfumeThumbnailUrl: String = addBucketUrl(story.perfume.thumbnailUrl),
    val userId : Long? = story.user.id,
    val userNickname: String? = story.user.nickname,
    val userProfileImageUrl: String? = story.user.profileImage?.url,
    val commentCount: Int = story.commentCount,
    val likeCount: Int = story.likeCount,
    val createdAt: LocalDateTime = story.createdAt,
    val tags: List<TagDto> = story.viewStoryTags().map { TagDto(it.tag) },
    val isOnlyMe: Boolean = story.isOnlyMe,
    var isLiked: Boolean? = false
)