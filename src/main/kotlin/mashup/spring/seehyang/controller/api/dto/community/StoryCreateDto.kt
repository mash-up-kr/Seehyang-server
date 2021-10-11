package mashup.spring.seehyang.controller.api.dto.community

data class StoryCreateRequest(
    val perfumeId: Long,
    val imageId: Long,
    val tags: List<String> = mutableListOf()
)

data class StoryCreateResponse(
    val story: StoryDto
)