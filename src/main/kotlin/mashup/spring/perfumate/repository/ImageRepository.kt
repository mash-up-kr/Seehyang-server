package mashup.spring.perfumate.repository

import mashup.spring.perfumate.domain.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long> {
}