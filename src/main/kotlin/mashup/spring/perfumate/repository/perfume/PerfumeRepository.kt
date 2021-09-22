package mashup.spring.perfumate.repository.perfume

import mashup.spring.perfumate.domain.entity.perfume.Perfume
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PerfumeRepository : JpaRepository<Perfume, Long>{
}