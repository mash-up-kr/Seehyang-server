package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.CommentCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.CommentCreateResponse
import mashup.spring.seehyang.controller.api.dto.community.CommentDeleteResponse
import mashup.spring.seehyang.controller.api.dto.community.CommentDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.service.CommentService
import org.springframework.web.bind.annotation.*

@ApiV1
class CommentApiController(
    private val commentService: CommentService
) {

    @PostMapping("/story/{id}/comment")
    fun createComment(
        user: User,
        @PathVariable(value = "id") storyId: Long,
        @RequestBody requestDto: CommentCreateRequest,
    ): SeehyangResponse<CommentCreateResponse> {
        if(user.isLogin().not())
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)

        val commentContents = requestDto.contents
            ?: throw NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT)

        val savedComment = commentService.addComment(user, storyId, commentContents)
        return SeehyangResponse(CommentCreateResponse(savedComment, userNickname = user.nickname!!))
    }

    @GetMapping("/story/{id}/comments")
    fun comments(
        @PathVariable(value = "id") storyId: Long,
        @RequestParam(value = "cursor") cursor: Long? = null,
    ): SeehyangResponse<List<CommentDto>>{
        val comments = commentService.getComments(storyId, cursor)
        val commentDtos = comments.map { CommentDto(it) }
        return SeehyangResponse(commentDtos)
    }

    @PostMapping("/comment/{id}/reply")
    fun createReplyComment(
        user: User,
        @PathVariable(value = "id") commentId: Long,
        @RequestBody requestDto: CommentCreateRequest,
    ): SeehyangResponse<CommentCreateResponse> {
        if(user.isLogin().not())
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)

        val commentContents = requestDto.contents
            ?: throw NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT)

        val savedComment = commentService.addReplyComment(user, commentId, commentContents)
        return SeehyangResponse(CommentCreateResponse(savedComment, userNickname = user.nickname!!))
    }

    @GetMapping("/comment/{id}/reply")
    fun replyComments(
        @PathVariable(value = "id") parentCommentId: Long,
        @RequestParam(value = "cursor") cursor: Long? = null,
    ): SeehyangResponse<List<CommentDto>>{
        val replyComments = commentService.getReplyComments(parentCommentId, cursor)
        val replyCommentDtos = replyComments.map { CommentDto(it) }
        return SeehyangResponse(replyCommentDtos)
    }

    @DeleteMapping("/comment/{id}")
    fun deleteComment(
        @PathVariable(value = "id") commentId: Long,
        user : User
    ): SeehyangResponse<CommentDeleteResponse>{
        if(user.isLogin().not())
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)

        val deletedId: Long = commentService.deleteComment(user, commentId)

        return SeehyangResponse(CommentDeleteResponse(deletedId))
    }

}