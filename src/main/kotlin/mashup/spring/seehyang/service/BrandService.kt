package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.perfume.BrandEditRequest
import mashup.spring.seehyang.repository.perfume.BrandRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class BrandService(
    val brandRepository: BrandRepository
) {
    //TODO : Brand 로 향수 검색하기 기능 추가
    fun edit(id: Long, brandEditRequest: BrandEditRequest) {
        val brand = brandRepository.findById(id).get()
        brand.name = brandEditRequest.name!!
        brand.koreanName = brandEditRequest.koreanName!!
    }
}