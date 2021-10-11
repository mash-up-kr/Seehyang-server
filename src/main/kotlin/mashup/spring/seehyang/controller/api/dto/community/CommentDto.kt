package mashup.spring.seehyang.controller.api.dto.community

import com.fasterxml.jackson.annotation.JsonIgnore
import mashup.spring.seehyang.domain.entity.community.Comment
import java.time.LocalDateTime

data class CommentDto (
    @JsonIgnore
    val comment: Comment,

    val id: Long = comment.id!!,
    val nickname: String? = comment.user.nickname,
    val createdAt : LocalDateTime = comment.createdAt,
    val contents: String = comment.contents,
    val replyCount: Int = comment.numOfReply,
    val likeCount: Int = comment.numOfLike,
    val dislikeCount: Int = comment.numOfDislike
)