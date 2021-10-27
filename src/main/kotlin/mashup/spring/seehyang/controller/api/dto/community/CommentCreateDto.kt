package mashup.spring.seehyang.controller.api.dto.community

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.domain.entity.community.Comment
import java.time.LocalDateTime

data class CommentCreateRequest(
    val contents: String? = null
)

data class CommentCreateResponse(
    @JsonIgnore
    val comment: Comment,
    val userNickname: String = comment.user.nickname!!,
    val id: Long = comment.id!!,
    val contents: String = comment.contents,
    val createdAt: LocalDateTime = comment.createdAt
)

data class CommentDeleteResponse(
    val commentId: Long
)