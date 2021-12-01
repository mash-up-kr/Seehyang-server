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
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class CommentApiController(
    private val storyService: StoryService
) {

    @GetMapping("/story/{storyId}/comment")
    fun getComments(
        @ApiIgnore userDto: UserDto?,
        @PathVariable(value = "storyId") storyId: Long,
        @RequestParam(value = "cursor", required = false) cursor: Long? = null,
    ): SeehyangResponse<List<CommentDto>>{

        val commentsDto = storyService.getComments(storyId, userDto, cursor)

        return SeehyangResponse(commentsDto)
    }



    @GetMapping("/story/{storyId}/comment/{id}")
    fun getReplyComments(
        @ApiIgnore userDto: UserDto?,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") parentCommentId: Long,
        @RequestParam(value = "cursor", required = false) cursor: Long? = null,
    ): SeehyangResponse<List<CommentDto>>{

        val replyComments = storyService.getReplyComments(storyId, parentCommentId, userDto, cursor)

        return SeehyangResponse(replyComments)
    }



    @PostMapping("/story/{storyId}/comment")
    fun createComment(
        @ApiIgnore userDto: UserDto?,
        @PathVariable(value = "storyId") storyId: Long,
        @RequestBody requestDto: CommentCreateRequest,
    ): SeehyangResponse<String> {

        if(userDto == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val commentContents = validateContents(requestDto.contents)

        storyService.addComment(storyId, userDto, commentContents)

        return SeehyangResponse("OK")
    }



    @PostMapping("/story/{storyId}/comment/{commentId}")
    fun createReplyComment(
        @ApiIgnore userDto: UserDto?,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long,
        @RequestBody requestDto: CommentCreateRequest,
    ): SeehyangResponse<String> {

        if(userDto == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val commentContents = validateContents(requestDto.contents)

        storyService.addReplyComment(storyId, commentId, userDto, commentContents)

        return SeehyangResponse("OK")
    }

    @PostMapping("/story/{storyId}/comment/{commentId}/like")
    fun createCommentLike(
        @ApiIgnore userDto: UserDto?,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long
    ): SeehyangResponse<Pair<String,String>> {

        if(userDto == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val isLiked:Boolean = storyService.likeComment(storyId, commentId, userDto)

        return SeehyangResponse(Pair("isLiked",isLiked.toString()))
    }

    @PostMapping("/story/{storyId}/comment/{commentId}/dislike")
    fun createCommentDislike(
        @ApiIgnore userDto: UserDto?,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long
    ): SeehyangResponse<Pair<String,String>> {

        if(userDto == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        val isDisliked:Boolean = storyService.dislikeComment(storyId, commentId, userDto)

        return SeehyangResponse(Pair("isDisLiked",isDisliked.toString()))
    }


    @DeleteMapping("/story/{storyId}/comment/{commentId}")
    fun deleteComment(
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long,
        @ApiIgnore userDto: UserDto?
    ): SeehyangResponse<CommentDeleteResponse>{

        if(userDto == null){
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        storyService.deleteComment(storyId,commentId,userDto)

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