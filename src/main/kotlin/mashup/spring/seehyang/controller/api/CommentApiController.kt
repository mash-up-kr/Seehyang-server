package mashup.spring.seehyang.controller.api

import io.swagger.annotations.ApiParam
import io.swagger.v3.oas.annotations.Parameter
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
import mashup.spring.seehyang.service.StoryService
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class CommentApiController(
    private val commentService: CommentService,
    private val storyService: StoryService
) {

    @GetMapping("/story/{id}/comments")
    fun getComments(
        @ApiIgnore user:User,
        @PathVariable(value = "id") storyId: Long,
        @RequestParam(value = "cursor", required = false) cursor: Long? = null,
    ): SeehyangResponse<List<CommentDto>>{
        val storyDto = storyService.getStoryDetail(user, storyId)
        val commentsDto = commentService.getComments(storyDto.id, cursor)
        return SeehyangResponse(commentsDto)
    }

    @GetMapping("/comment/{id}/reply")
    fun replyComments(
        @PathVariable(value = "id") parentCommentId: Long,
        @RequestParam(value = "cursor", required = false) cursor: Long? = null,
    ): SeehyangResponse<List<CommentDto>>{
        val replyComments = commentService.getReplyComments(parentCommentId, cursor)
        return SeehyangResponse(replyComments)
    }

    @PostMapping("/story/{id}/comment")
    fun createComment(
        @ApiIgnore user: User,
        @PathVariable(value = "id") storyId: Long,
        @RequestBody requestDto: CommentCreateRequest,
    ): SeehyangResponse<CommentDto> {

        if(user.isLogin().not())
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)

        val commentContents = requestDto.contents
            ?: throw NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT)

        commentService.addComment(user, storyId, commentContents)
        return SeehyangResponse()
    }



    @PostMapping("/comment/{id}/reply")
    fun createReplyComment(
        @ApiIgnore user: User,
        @PathVariable(value = "id") commentId: Long,
        @RequestBody requestDto: CommentCreateRequest,
    ): SeehyangResponse<CommentDto> {
        if(user.isLogin().not())
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)

        val commentContents = requestDto.contents
            ?: throw NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT)

        val savedComment = commentService.addReplyComment(user, commentId, commentContents)
        return SeehyangResponse()
    }



    @DeleteMapping("/comment/{id}")
    fun deleteComment(
        @PathVariable(value = "id") commentId: Long,
        @ApiIgnore user : User
    ): SeehyangResponse<CommentDeleteResponse>{
        if(user.isLogin().not())
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)

        val deletedId: Long = commentService.deleteComment(user, commentId)

        return SeehyangResponse(CommentDeleteResponse(deletedId))
    }

}