package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.CommentCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.CommentDeleteResponse
import mashup.spring.seehyang.controller.api.dto.community.CommentDto
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.service.StoryService
import mashup.spring.seehyang.service.auth.UserId
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class CommentApiController(
    private val storyService: StoryService
) {

    @GetMapping("/story/{storyId}/comment")
    fun getComments(
        @ApiIgnore userId: UserId?,
        @PathVariable(value = "storyId") storyId: Long,
        @RequestParam(value = "cursor", required = false) cursor: Long? = null,
    ): SeehyangResponse<List<CommentDto>>{

        val commentsDto = storyService.getComments(storyId, userId, cursor)

        return SeehyangResponse(commentsDto)
    }



    @GetMapping("/story/{storyId}/comment/{id}")
    fun getReplyComments(
        @ApiIgnore userId: UserId?,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") parentCommentId: Long,
        @RequestParam(value = "cursor", required = false) cursor: Long? = null,
    ): SeehyangResponse<List<CommentDto>>{

        val replyComments = storyService.getReplyComments(storyId, parentCommentId, userId, cursor)

        return SeehyangResponse(replyComments)
    }



    @PostMapping("/story/{storyId}/comment")
    fun createComment(
        @ApiIgnore userId: UserId?,
        @PathVariable(value = "storyId") storyId: Long,
        @RequestBody requestDto: CommentCreateRequest,
    ): SeehyangResponse<String> {

        if(userId == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val commentContents = validateContents(requestDto.contents)

        storyService.addComment(storyId, userId, commentContents)

        return SeehyangResponse("OK")
    }



    @PostMapping("/story/{storyId}/comment/{commentId}")
    fun createReplyComment(
        @ApiIgnore userId: UserId?,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long,
        @RequestBody requestDto: CommentCreateRequest,
    ): SeehyangResponse<String> {

        if(userId == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val commentContents = validateContents(requestDto.contents)

        storyService.addReplyComment(storyId, commentId, userId, commentContents)

        return SeehyangResponse("OK")
    }

    @PostMapping("/story/{storyId}/comment/{commentId}/like")
    fun createCommentLike(
        @ApiIgnore userId: UserId?,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long
    ): SeehyangResponse<Pair<String,String>> {

        if(userId == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val isLiked:Boolean = storyService.likeComment(storyId, commentId, userId)

        return SeehyangResponse(Pair("isLiked",isLiked.toString()))
    }

    @PostMapping("/story/{storyId}/comment/{commentId}/dislike")
    fun createCommentDislike(
        @ApiIgnore userId: UserId?,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long
    ): SeehyangResponse<Pair<String,String>> {

        if(userId == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val isDisliked:Boolean = storyService.dislikeComment(storyId, commentId, userId)

        return SeehyangResponse(Pair("isDisLiked",isDisliked.toString()))
    }


    @DeleteMapping("/story/{storyId}/comment/{commentId}")
    fun deleteComment(
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long,
        @ApiIgnore userId: UserId?,
    ): SeehyangResponse<CommentDeleteResponse>{

        if(userId == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        storyService.deleteComment(storyId,commentId,userId)

        return SeehyangResponse(CommentDeleteResponse(commentId))
    }

    /**
     * ===========Private Methods =============
     */

    private fun validateContents(contents: String?): String{
        if(contents.isNullOrBlank()){
            throw BadRequestException(SeehyangStatus.CONTENTS_IS_EMPTY)
        }
        return contents
    }

}