package mashup.spring.seehyang.repository.perfume

import mashup.spring.seehyang.domain.entity.perfume.PerfumeNote
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PerfumeNoteRepository : JpaRepository<PerfumeNote, Long> {
}