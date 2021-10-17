package mashup.spring.seehyang.repository.perfume

import mashup.spring.seehyang.domain.entity.perfume.Perfume
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
                   "where korean_name like CONCAT('%',:name,'%') and id < :cursor " +
                   "order by id desc " +
                   "limit :limit")
    fun findByKoreanName(@Param("name") name: String, @Param("cursor") cursor: Long, @Param("limit") limit: Int): List<Perfume>

    fun findTop10ByOrderByLikeCountDesc(): List<Perfume>

    fun findTop10ByKoreanNameOrderByLikeCountDesc(name: String): List<Perfume>
}