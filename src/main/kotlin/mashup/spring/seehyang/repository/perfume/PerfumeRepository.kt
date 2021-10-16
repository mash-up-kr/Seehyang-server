package mashup.spring.seehyang.repository.perfume

import mashup.spring.seehyang.domain.entity.perfume.Perfume
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PerfumeRepository : JpaRepository<Perfume, Long>{
    @Query("select p from Perfume p where size(p.stories) >= 5")
    fun findByStoryLengthGreaterThan(): List<Perfume>

    fun findTop10ByOrderByLikeCountDesc(): List<Perfume>
}