package mashup.spring.seehyang.repository.perfume


import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.perfume.PerfumeLike
import mashup.spring.seehyang.domain.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PerfumeLikeRepository : JpaRepository<PerfumeLike, Long> {
    fun findByUserAndPerfume(user: User, perfume: Perfume) : Optional<PerfumeLike>
}