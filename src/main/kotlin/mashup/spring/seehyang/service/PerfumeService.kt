package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeEditRequest
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

    fun edit(id: Long, request: PerfumeEditRequest): Perfume{
        val perfume = perfumeRepository.findById(id).get()
        // TODO 더 세심한 체크 ..
        perfume.name = request.name!!
        perfume.koreanName = request.koreanName!!
        return perfume
    }
}