package mashup.spring.perfumate.service

import mashup.spring.perfumate.domain.entity.perfume.Perfume
import mashup.spring.perfumate.repository.perfume.PerfumeRepository
import org.springframework.stereotype.Service

@Service
class PerfumeService(
    private val perfumeRepository: PerfumeRepository
) {
    fun get(id: Long) : Perfume {
        return perfumeRepository.getById(id)
    }
}