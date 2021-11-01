package mashup.spring.seehyang.repository.perfume

import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import org.hibernate.annotations.BatchSize
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PerfumeRepository : JpaRepository<Perfume, Long>{
    @Query("select p from Perfume p where size(p.stories) >= 5")
    fun findByStoryLengthGreaterThan(): List<Perfume>


    @Query(nativeQuery = true,
           value = "select * from perfume " +
                   "where korean_name like concat('%', :name,'%') and id < :cursor " +
                   "order by id desc " +
                   "limit :limit")
    fun findByKoreanName(@Param("name") name: String, @Param("cursor") cursor: Long, @Param("limit") limit: Int): List<Perfume>

    @Query(nativeQuery = true,
           value = "select * from perfume " +
                   "where upper(name) like concat('%', upper(:name),'%') and id < :cursor " +
                   "order by id desc " +
                   "limit :limit")
    fun findByEngName(@Param("name") name: String, @Param("cursor") cursor: Long, @Param("limit") limit: Int): List<Perfume>


    @Query(nativeQuery = true,
           value = "select * from perfume " +
                   "where  (like_count < :likeCursor or (like_count = :like_count and id < :idCursor)) " +
                   "order by like_count desc, id desc " +
                   "limit :limit")
    fun findSteadyPerfume(@Param("idCursor") idCursor: Long, @Param("likeCursor") likeCursor: Int, @Param("limit") limit: Int): List<Perfume>

    fun findTop10ByOrderByLikeCountDesc(): List<Perfume>
    fun findTop6ByOrderByLikeCountDescIdDesc(): List<Perfume>
    fun findTop10ByKoreanNameContainsOrderByIdDesc(name: String): List<Perfume>
    fun findTop10ByNameContainsIgnoreCaseOrderByIdDesc(name: String): List<Perfume>


    @Query("select p from Perfume p join fetch p.brand b where p.id in :ids")
    fun findByIds(@Param("ids") storyIds: List<Long>): List<Perfume>
}