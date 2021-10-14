package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.CommentCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.CommentCreateResponse
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.service.CommentService
import mashup.spring.seehyang.service.UserService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.HttpServletRequest

@ApiV1
class CommentApiController(
    private val userService: UserService,
    private val commentService: CommentService
) {

    @Authenticated
    @PostMapping("/story/{id}/comment")
    fun create(
        @PathVariable(value = "id") storyId: Long,
        requestDto: CommentCreateRequest,
        request: HttpServletRequest
    ): SeehyangResponse<Any> {
        val user = userService.getUser(request)

        val commentContents = requestDto.contents?: throw RuntimeException("내용을 작성 해야 합니다.")

        val savedComment = commentService.addComment(user, storyId, commentContents)
        return SeehyangResponse(CommentCreateResponse(savedComment.id!!))
    }
}