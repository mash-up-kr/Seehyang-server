package mashup.spring.seehyang.domain.entity.perfume

import mashup.spring.seehyang.createTestUser
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager

@DataJpaTest
class PerfumeTest @Autowired constructor(
    val perfumeRepository: PerfumeRepository,
    val brandRepository: BrandRepository,
    val userRepository: UserRepository,
    val entityManager: EntityManager
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
        userRepository.save(createTestUser(true))
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

    @Test
    fun perfume_like_test(){
        perfumeRepository.save(testPerfume)
        val findPerfume: Perfume = perfumeRepository.findAll()[0]
        val user = userRepository.findAll()[0]
        findPerfume.likePerfume(user)

        entityManager.flush()
        entityManager.clear()

        val testPerfume: Perfume = perfumeRepository.findAll()[0]

        assertEquals(testPerfume.likeCount, 1)
    }

    @Test
    fun perfume_dislike_test(){
        perfumeRepository.save(testPerfume)
        val findPerfume: Perfume = perfumeRepository.findAll()[0]
        val user = userRepository.findAll()[0]
        //좋아요 후 취소
        findPerfume.likePerfume(user)
        findPerfume.likePerfume(user)

        entityManager.flush()
        entityManager.clear()

        val testPerfume: Perfume = perfumeRepository.findAll()[0]

        assertEquals(testPerfume.likeCount, 0)
    }
}