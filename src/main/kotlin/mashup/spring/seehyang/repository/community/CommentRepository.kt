package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.domain.entity.community.Comment
import mashup.spring.seehyang.domain.entity.community.Story
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = ["parent", "story","user"])
    @Query("select c from Comment c " +
                        "where c.parent is null and c.story = :story and c.id < :cursor " +
                        "order by c.id desc")
    fun findCommentsByStoryId(@Param("story") story: Story, @Param("cursor") cursor: Long,pageable: Pageable) : List<Comment>

    @EntityGraph(attributePaths = ["parent", "story","user"])
    fun findTop20ByStoryIdOrderByIdDesc(storyId: Long) : List<Comment>

    @EntityGraph(attributePaths = ["parent", "story","user"])
    @Query("select c from Comment c " +
                "where c.parent.id = :parentId and c.id < :cursor " +
                "order by c.id desc ")
    fun findReplyCommentsByStoryId(@Param("parentId") parentId:Long, @Param("cursor") cursor: Long, pageable: Pageable) : List<Comment>

    @EntityGraph(attributePaths = ["parent", "story","user"])
    fun findTop20ByParentIdOrderByIdDesc(parentId: Long) : List<Comment>

    @EntityGraph(attributePaths = ["parent", "story", "user"])
    override fun findById(id: Long): Optional<Comment>
}