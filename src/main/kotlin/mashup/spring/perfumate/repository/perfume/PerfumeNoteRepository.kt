package mashup.spring.perfumate.repository.perfume

import mashup.spring.perfumate.domain.entity.perfume.PerfumeNote
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PerfumeNoteRepository : JpaRepository<PerfumeNote, Long> {
}