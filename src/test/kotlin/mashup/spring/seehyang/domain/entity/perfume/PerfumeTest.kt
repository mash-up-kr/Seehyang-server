package mashup.spring.seehyang.domain.entity.perfume

import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class PerfumeTest @Autowired constructor(
    val perfumeRepository: PerfumeRepository,
    val brandRepository: BrandRepository
){

    private val testBrand = Brand(
        name ="Chanel",
        koreanName = "샤넬"
    )

    private val testPerfume = Perfume(
        name = "theBestPerfume",
        koreanName = "최고의향수",
        type = PerfumeType.EAU_DE,
        gender = Gender.BOTH,
        thumbnailUrl = "best/best.jpg",
        brand = testBrand
    )

    @BeforeEach
    fun before() {
        brandRepository.save(testBrand)
    }

    @Test
    fun save_perfume_test() {
        perfumeRepository.save(testPerfume)
    }

    @Test
    fun get_perfume_test() {
        perfumeRepository.save(testPerfume)
        val findPerfume: Perfume = perfumeRepository.findAll()[0]

        assertEquals("theBestPerfume", findPerfume.name)
        assertEquals("최고의향수", findPerfume.koreanName)
        assertEquals(Gender.BOTH, findPerfume.gender)

        assertEquals("Chanel", findPerfume.brand.name)
    }
}