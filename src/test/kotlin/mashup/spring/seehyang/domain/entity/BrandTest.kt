package mashup.spring.seehyang.domain.entity.perfume

import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager

@DataJpaTest
class BrandTest @Autowired constructor(
    val brandRepository: BrandRepository,
    val perfumeRepository: PerfumeRepository,
    val entityManager: EntityManager
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

    @Test
    fun get_brands_perfumes() {
        brandRepository.save(testBrand)

        val testPerfume = Perfume(
            name = "theBestPerfume",
            koreanName = "최고의향수",
            type = PerfumeType.EAU_DE,
            gender = Gender.BOTH,
            thumbnailUrl = "best/best.jpg",
            brand = testBrand
        )
        perfumeRepository.save(testPerfume)
        entityManager.clear()

        val findBrand = brandRepository.findAll()[0]
        assertEquals("theBestPerfume", findBrand.perfumes[0].name)

    }
}