package mashup.spring.seehyang.controller.api.dto.home

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.domain.entity.community.Story

data class HotStoryDto(
    @JsonIgnore
    val storyEntityList: List<Story>,

    val stories: List<HotStoryDetailDto> = storyEntityList.map { HotStoryDetailDto(it) }
)

data class HotStoryDetailDto(
    @JsonIgnore
    val story: Story,
    val id: Long = story.id!!,
    val perfumeImageUrl: String = story.perfume.thumbnailUrl,
    val thumbnailUrl: String = story.image.url,
    val userProfileImageUrl: String? = story.user.profileImage?.url,
    val userNickname: String? = story.user.nickname
)
