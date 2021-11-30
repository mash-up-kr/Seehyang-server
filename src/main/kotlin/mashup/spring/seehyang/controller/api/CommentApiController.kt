package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.dto.community.CommentCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.CommentDeleteResponse
import mashup.spring.seehyang.controller.api.dto.community.CommentDto
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.exception.BadRequestException
import mashup.spring.seehyang.service.StoryService
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class CommentApiController(
    private val storyService: StoryService
) {

    @GetMapping("/story/{storyId}/comment")
    fun getComments(
        @ApiIgnore userDto: UserDto,
        @PathVariable(value = "storyId") storyId: Long,
        @RequestParam(value = "cursor", required = false) cursor: Long? = null,
    ): SeehyangResponse<List<CommentDto>>{

        val commentsDto = storyService.getComments(storyId, userDto, cursor)

        return SeehyangResponse(commentsDto)
    }



    @GetMapping("/story/{storyId}/comment/{id}")
    fun replyComments(
        @ApiIgnore userDto: UserDto,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "id") parentCommentId: Long,
        @RequestParam(value = "cursor", required = false) cursor: Long? = null,
    ): SeehyangResponse<List<CommentDto>>{

        val replyComments = storyService.getReplyComments(storyId, parentCommentId, userDto, cursor)

        return SeehyangResponse(replyComments)
    }



    @PostMapping("/story/{storyId}/comment")
    fun createComment(
        @ApiIgnore userDto: UserDto,
        @PathVariable(value = "storyId") storyId: Long,
        @RequestBody requestDto: CommentCreateRequest,
    ): SeehyangResponse<String> {


        val commentContents = contentsValidation(requestDto.contents)

        storyService.addComment(storyId, userDto, commentContents)

        return SeehyangResponse("OK")
    }



    @PostMapping("/story/{storyId}/comment/{commentId}")
    fun createReplyComment(
        @ApiIgnore userDto: UserDto,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long,
        @RequestBody requestDto: CommentCreateRequest,
    ): SeehyangResponse<String> {

        val commentContents = contentsValidation(requestDto.contents)

        storyService.addReplyComment(storyId, commentId, userDto, commentContents)

        return SeehyangResponse("OK")
    }



    @DeleteMapping("/story/{storyId}/comment/{commentId}")
    fun deleteComment(
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long,
        @ApiIgnore userDto: UserDto
    ): SeehyangResponse<CommentDeleteResponse>{


        storyService.deleteComment(storyId,commentId,userDto)

        return SeehyangResponse(CommentDeleteResponse(commentId))
    }

    /**
     * ===========Private Methods =============
     */

    private fun contentsValidation(contents: String?): String{
        if(contents.isNullOrBlank()){
            throw BadRequestException(SeehyangStatus.CONTENTS_IS_EMPTY)
        }
        return contents
    }

}