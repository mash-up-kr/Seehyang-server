package mashup.spring.seehyang.controller.api.dto.home

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.perfume.Perfume

data class TodaySeehyangDto(
    @JsonIgnore
    val perfumeEntity: Perfume,
    @JsonIgnore
    val storyEntityList: List<Story>,

    val perfume: TodayPerfume = TodayPerfume(perfumeEntity.id!!, perfumeEntity.thumbnailUrl, perfumeEntity.koreanName),
    val stories: List<TodayStory> = storyEntityList.map { TodayStory(it.id!!, it.image.url, it.user.profileImage?.url, it.user.nickname, it.likeCount) }
)

data class TodayPerfume(
    val id: Long,
    val thumbnail: String,
    val name: String
)

data class TodayStory(
    val id: Long,
    val imageUrl: String,
    val userProfileImage: String?,
    val userNickname: String,
    val likeCount: Int
)