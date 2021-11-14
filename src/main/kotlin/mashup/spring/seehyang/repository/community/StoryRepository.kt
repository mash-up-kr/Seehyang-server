package mashup.spring.seehyang.repository.community

import mashup.spring.seehyang.domain.entity.community.Story
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StoryRepository : JpaRepository<Story, Long>{

    @EntityGraph(attributePaths = ["user","image","perfume"])
    override fun findById(@Param("id") id:Long): Optional<Story>

    @EntityGraph(attributePaths = ["user","image","perfume"])
    fun findTop10ByPerfumeIdOrderByLikeCountDesc(perfumeId: Long?): List<Story>

    @EntityGraph(attributePaths = ["user","image","perfume"])
    @Query("select s from Story s " +
                "where s.perfume.id = :perfumeId and s.id < :cursor " +
                "order by s.id desc")
    fun findStoryByPerfumeId(@Param("perfumeId") perfumeId: Long, @Param("cursor") cursor: Long, pageable: Pageable) : List<Story>


    @EntityGraph(attributePaths = ["user","image","perfume"])
    fun findTop10ByPerfumeIdOrderByIdDesc(perfumeId: Long) : List<Story>


    @EntityGraph(attributePaths = ["user","image","perfume"])
    @Query("select s " +
                   "from Story s " +
                   "where s.id in :ids")
    fun findByIds(@Param("ids") storyIds: List<Long>): List<Story>

    @EntityGraph(attributePaths = ["user","image","perfume"])
    fun findTop10ByOrderByLikeCountDesc(): List<Story>

}