package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.community.Comment
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.exception.UnauthorizedException
import mashup.spring.seehyang.repository.community.CommentRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.user.UserRepository

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CommentService(
    val storyRepository: StoryRepository,
    val commentRepository: CommentRepository,
    val userRepository: UserRepository
) {

    private val PAGE_SIZE: Int = 20

    fun addComment(
        user: User,
        storyId: Long,
        commentContents: String
    ): Comment{
        val managedUser = userRepository.findById(user.id?:throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER))
            .orElseThrow{NotFoundException(SeehyangStatus.NOT_FOUND_USER)}
        val story = storyRepository.findById(storyId).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT) }
        val comment = Comment(contents = commentContents, story = story, user = managedUser)

        // 동시성 및 잠금에 대해서 생각 해보기
        story.addCommentCount()
        return commentRepository.save(comment)
    }

    // QueryDSL 이었다면...
    @Transactional(readOnly = true)
    fun getComments(storyId: Long, cursor: Long?): List<Comment>
            = if (cursor == null) commentRepository.findTop20ByStoryIdOrderByIdDesc(storyId)
    else commentRepository.findCommentsByStoryId(storyId, cursor, PAGE_SIZE)

    fun addReplyComment(
        user: User,
        commentId: Long,
        commentContents: String
    ): Comment{
        val managedUser = userRepository.findById(user.id?:throw UnauthorizedException(SeehyangStatus.UNAUTHORIZED_USER))
            .orElseThrow{NotFoundException(SeehyangStatus.NOT_FOUND_USER)}
        val comment = commentRepository.findById(commentId).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_COMMENT) }
        val replyComment = Comment(contents = commentContents, parent = comment, story = comment.story, user = managedUser)

        // 동시성 및 잠금에 대해서 생각 해보기
        comment.story.addCommentCount()
        return commentRepository.save(replyComment)
    }

    @Transactional(readOnly = true)
    fun getReplyComments(parentCommentId: Long, cursor: Long?): List<Comment>
            = if (cursor == null) commentRepository.findTop20ByParentIdOrderByIdDesc(parentCommentId)
    else commentRepository.findReplyCommentsByStoryId(parentCommentId, cursor, PAGE_SIZE)


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