package mashup.spring.seehyang.repository

import mashup.spring.seehyang.domain.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long> {
}