package mashup.spring.seehyang.controller.api.dto.community

data class CommentCreateRequest(
    val contents: String? = null
)

data class CommentCreateResponse(
    val id: Long
)