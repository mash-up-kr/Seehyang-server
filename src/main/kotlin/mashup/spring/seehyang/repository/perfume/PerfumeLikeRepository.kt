package mashup.spring.seehyang.repository.perfume


import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.perfume.PerfumeLike
import mashup.spring.seehyang.domain.entity.user.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface PerfumeLikeRepository : JpaRepository<PerfumeLike, Long> {

    @Query("select pl " +
                   "from PerfumeLike pl " +
                   "join fetch pl.user u " +
                   "join fetch pl.perfume p " +
                   "where u =:user and p =:perfume")
    fun findByUserAndPerfume(@Param("user") user: User,@Param("perfume") perfume: Perfume) : Optional<PerfumeLike>

    @Query("select p.id " +
                   "from PerfumeLike l join l.perfume p " +
                   "where l.createdAt >:from and l.createdAt <:to " +
                   "group by p " +
                   "order by count(l) desc")
    fun findPerfumeIdByRecentLike(@Param("from")from: LocalDateTime,
                                @Param("to") to: LocalDateTime, pageable: Pageable): List<Long>
}