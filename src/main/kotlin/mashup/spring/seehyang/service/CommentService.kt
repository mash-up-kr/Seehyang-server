package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.community.CommentCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.CommentDto
import mashup.spring.seehyang.domain.entity.community.Comment
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.repository.community.CommentRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CommentService(
    val storyRepository: StoryRepository,
    val commentRepository: CommentRepository
) {

    private val PAGE_SIZE: Int = 20

    fun addComment(
        user: User,
        storyId: Long,
        commentContents: String
    ): Comment{
        val story = storyRepository.findById(storyId).orElseThrow { RuntimeException("Entity Not Fount: Story") }
        val comment = Comment(contents = commentContents, story = story, user = user)
        return commentRepository.save(comment)
    }

    // QueryDSL 이었다면...
    @Transactional(readOnly = true)
    fun getComments(storyId: Long, cursor: Long?): List<Comment>
            = if (cursor == null) commentRepository.findTop20ByStoryIdOrderByIdDesc(storyId)
    else commentRepository.findCommentsByStoryId(storyId, cursor, PAGE_SIZE)


    @Transactional(readOnly = true)
    fun getTopLevelComment(storyId: Long, pageable: Pageable)
    = commentRepository.findFirstLevelCommentsByStoryId(storyId, pageable).map { CommentDto(it) }

    @Transactional(readOnly = true)
    fun getSecondLevelComment(commentId:Long, pageable: Pageable)
    = commentRepository.findSecondLevelCommentsByCommentId(commentId, pageable).map{CommentDto(it)}
}