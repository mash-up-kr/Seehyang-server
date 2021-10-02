package mashup.spring.seehyang.service

import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PerfumeService(
    private val perfumeRepository: PerfumeRepository
) {
    @Transactional(readOnly = true)
    fun get(id: Long) : Perfume {
        return perfumeRepository.getById(id)
    }
}