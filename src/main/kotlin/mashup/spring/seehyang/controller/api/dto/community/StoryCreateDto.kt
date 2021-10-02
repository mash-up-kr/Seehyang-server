package mashup.spring.seehyang.controller.api.dto.community

data class StoryCreateRequest(
    val perfumeId: Long,
    val contents: String,
    val imageId: Long,
    val tags: MutableList<String> = mutableListOf()
)

data class StoryCreateResponse(
    val story: StoryDto
)