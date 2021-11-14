package mashup.spring.seehyang.repository.perfume

import mashup.spring.seehyang.domain.entity.perfume.Perfume
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PerfumeRepository : JpaRepository<Perfume, Long>{

    @EntityGraph(attributePaths = ["brand"])
    override fun findById(id: Long): Optional<Perfume>

    @Query("select p from Perfume p " +
                   "join fetch p.brand b " +
                   "where size(p.stories) >= 5")
    fun findByStoryLengthGreaterThan(): List<Perfume>


    @Query("select p from Perfume p " +
                   "join fetch p.brand b " +
                   "where p.koreanName like %:name% and p.id < :cursor " +
                   "order by p.id desc")
    fun findByKoreanName(@Param("name") name: String, @Param("cursor") cursor: Long, pageable: Pageable): List<Perfume>

    @Query("select p from Perfume p " +
                   "join fetch p.brand b " +
                   "where upper(p.name) like upper(concat('%',:name,'%')) and p.id < :cursor " +
                   "order by p.id desc")
    fun findByEngName(@Param("name") name: String, @Param("cursor") cursor: Long, pageable: Pageable): List<Perfume>


    @Query("select p from Perfume p " +
                   "join fetch p.brand b " +
                   "where (p.likeCount < :likeCursor or (p.likeCount = :like_count and p.id < :idCursor)) " +
                   "order by p.likeCount desc, p.id desc")
    fun findSteadyPerfume(@Param("idCursor") idCursor: Long, @Param("likeCursor") likeCursor: Int, pageable: Pageable): List<Perfume>

    @Query("select p from Perfume p " +
                   "join fetch p.brand b " +
                   "where p.id < :idCursor " +
                   "order by p.id desc")
    fun findSteadyPerfume(@Param("idCursor") idCursor: Long, pageable: Pageable): List<Perfume>

    @EntityGraph(attributePaths = ["brand"])
    fun findTop10ByOrderByLikeCountDesc(): List<Perfume>
    @EntityGraph(attributePaths = ["brand"])
    fun findTop6ByOrderByLikeCountDescIdDesc(): List<Perfume>
    @EntityGraph(attributePaths = ["brand"])
    fun findTop10ByKoreanNameContainsOrderByIdDesc(name: String): List<Perfume>
    @EntityGraph(attributePaths = ["brand"])
    fun findTop10ByNameContainsIgnoreCaseOrderByIdDesc(name: String): List<Perfume>


    @Query("select p from Perfume p " +
                   "join fetch p.brand b " +
                   "where p.id in :ids")
    fun findByIds(@Param("ids") storyIds: List<Long>): List<Perfume>
}