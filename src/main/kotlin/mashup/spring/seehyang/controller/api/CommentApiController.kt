package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.CommentCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.CommentCreateResponse
import mashup.spring.seehyang.controller.api.dto.community.CommentDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.service.CommentService
import mashup.spring.seehyang.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

@ApiV1
class CommentApiController(
    private val userService: UserService,
    private val commentService: CommentService
) {

    @Authenticated
    @PostMapping("/story/{id}/comment")
    fun createComment(
        @PathVariable(value = "id") storyId: Long,
        requestDto: CommentCreateRequest,
        request: HttpServletRequest
    ): SeehyangResponse<CommentCreateResponse> {
        val user = userService.getUser(request)

        val commentContents = requestDto.contents?: throw RuntimeException("내용을 작성 해야 합니다.")

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

    @Authenticated
    @PostMapping("/comment/{id}/reply")
    fun createReplyComment(
        @PathVariable(value = "id") commentId: Long,
        requestDto: CommentCreateRequest,
        request: HttpServletRequest
    ): SeehyangResponse<CommentCreateResponse> {
        val user = userService.getUser(request)

        val commentContents = requestDto.contents?: throw RuntimeException("내용을 작성 해야 합니다.")

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
}