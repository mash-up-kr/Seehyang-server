package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.community.CommentDto
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.community.Comment
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.repository.community.CommentRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.springframework.data.domain.PageRequest

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CommentService(
    val storyService: StoryService,
    val userService: UserService,
    val commentRepository: CommentRepository,
) {

    private val PAGE_SIZE: Int = 20

    fun addComment(
        user: User,
        storyId: Long,
        commentContents: String
    ): Unit{

        val managedUser = userService.getUser(user.id)

        val story = storyService.getStoryDetail(user,storyId).story

        val comment = Comment(contents = commentContents, story = story, user = managedUser)

        storyService.addComment(comment)
        // 동시성 및 잠금에 대해서 생각 해보기

    }

    // QueryDSL 이었다면...
    @Transactional(readOnly = true)
    fun getComments(user: User, storyDto: StoryDto,cursor: Long?): List<CommentDto> {

        val story = storyService.getStoryDetail(user, storyDto.id).story

        return if (cursor == null) {
            commentRepository.findTop20ByStoryIdOrderByIdDesc(story.id!!).map { CommentDto(it) }
        }else {
            commentRepository.findCommentsByStoryId(story, cursor, PageRequest.ofSize(PAGE_SIZE)).map { CommentDto(it) }
        }
    }


    fun addReplyComment(
        user: User,
        commentId: Long,
        commentContents: String
    ): Unit{

        val managedUser = userService.getUser(user.id)

        val comment = commentRepository.findById(commentId).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT) }

        val replyComment = Comment(contents = commentContents, parent = comment, story = comment.story, user = managedUser)

        storyService.addComment(replyComment)
        // 동시성 및 잠금에 대해서 생각 해보기
    }


    @Transactional(readOnly = true)
    fun getReplyComments(user: User, parentCommentId: Long, cursor: Long?): List<CommentDto> {
        val comment = commentRepository.findById(parentCommentId).orElseThrow{NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT)}

        //TODO storyService 의 method를 쓰는것 한 번 더 고려해볼것.
        //TODO valid method 아니라 쿼리단계에서 걸러주는것은 어떤지?
        storyService.validateOnlyMe(user, comment.story)

        return if (cursor == null) {
            commentRepository.findTop20ByParentIdOrderByIdDesc(parentCommentId).map { CommentDto(it) }
        }else {
            commentRepository.findReplyCommentsByStoryId(parentCommentId, cursor, PageRequest.ofSize(PAGE_SIZE)).map { CommentDto(it) }
        }
    }

    fun deleteComment(user: User, id: Long): Long {
        val comment = commentRepository.findById(id).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT) }

        if (comment.user.id == user.id) {
            commentRepository.deleteById(id)
        } else {
            throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER)
        }

        return comment.id!!
    }
}