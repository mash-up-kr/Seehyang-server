package mashup.spring.seehyang.repository.perfume

import mashup.spring.seehyang.domain.entity.perfume.Perfume
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PerfumeRepository : JpaRepository<Perfume, Long>{
}