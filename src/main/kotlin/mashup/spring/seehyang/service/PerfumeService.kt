package mashup.spring.seehyang.service

import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.springframework.stereotype.Service

@Service
class PerfumeService(
    private val perfumeRepository: PerfumeRepository
) {
    fun get(id: Long) : Perfume {
        return perfumeRepository.getById(id)
    }
}