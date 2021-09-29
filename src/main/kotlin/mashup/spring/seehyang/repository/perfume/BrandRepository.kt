package mashup.spring.seehyang.repository.perfume

import mashup.spring.seehyang.domain.entity.perfume.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository : JpaRepository<Brand, Long> {
}