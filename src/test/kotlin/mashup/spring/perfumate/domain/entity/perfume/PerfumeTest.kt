package mashup.spring.perfumate.domain.entity.perfume

import mashup.spring.perfumate.repository.perfume.PerfumeRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class PerfumeTest @Autowired constructor(
    val perfumeRepository: PerfumeRepository
){

    private val testPerfume = Perfume(
        1L,
        "theBestPerfume",
        "최고의향수",
        Gender.BOTH,
        "best/best.jpg"
    )

    @Test
    fun save_perfume_test() {
        perfumeRepository.save(testPerfume)
    }

    @Test
    fun get_perfume_test() {
        perfumeRepository.save(testPerfume)
        val findPerfume: Perfume = perfumeRepository.findById(1L).get()

        assertEquals(1L, findPerfume.id)
        assertEquals("theBestPerfume", findPerfume.name)
        assertEquals("최고의향수", findPerfume.koreanName)
        assertEquals(Gender.BOTH, findPerfume.gender)
    }
}