package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.community.CommentDto
import mashup.spring.seehyang.domain.entity.community.Comment
import mashup.spring.seehyang.repository.community.CommentRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CommentService(
    val commentRepository: CommentRepository
) {

    //fun addComment(storyId: Long){
        //commentRepository.
    //}

    @Transactional(readOnly = true)
    fun getTopLevelComment(storyId: Long, pageable: Pageable)
    = commentRepository.findFirstLevelCommentsByStoryId(storyId, pageable).map { CommentDto(it) }

    @Transactional(readOnly = true)
    fun getSecondLevelComment(commentId:Long, pageable: Pageable)
    = commentRepository.findSecondLevelCommentsByCommentId(commentId, pageable).map{CommentDto(it)}


}