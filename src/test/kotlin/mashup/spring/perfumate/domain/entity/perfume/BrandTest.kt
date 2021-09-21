package mashup.spring.perfumate.domain.entity.perfume

import mashup.spring.perfumate.repository.perfume.BrandRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class BrandTest @Autowired constructor(
    val brandRepository: BrandRepository
){

    private val testBrand = Brand(
        name ="Chanel",
        koreanName = "샤넬"
    )

    @Test
    fun save_brand_test() {
        brandRepository.save(testBrand)
    }

    @Test
    fun get_brand_test() {
        brandRepository.save(testBrand)
        val findBrand = brandRepository.findAll().get(0)

        assertEquals("Chanel", findBrand.name)
        assertEquals("샤넬", findBrand.koreanName)
    }
}