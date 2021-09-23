package mashup.spring.perfumate.domain.entity

import mashup.spring.perfumate.domain.entity.perfume.Brand
import mashup.spring.perfumate.repository.perfume.BrandRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.util.ReflectionTestUtils
import javax.persistence.EntityManager

@DataJpaTest
class BaseTimeEntityTest @Autowired constructor(
    val brandRepository: BrandRepository,
    val entityManager: EntityManager
) {

    /**
     * 브랜드 엔터티를 대상으로 Jpa Auditing 테스트
     * */
    private val testBrand = Brand(
        name ="Chanel",
        koreanName = "샤넬"
    )

    @Test
    fun createdAtTest() {
        val savedBrand = brandRepository.save(testBrand)

        assertNotNull(savedBrand.createdAt)
    }

    /**
     * updated test 할 때는 jpa repository save 가 아닌 EntityManager 를 통해 수정
     * ref: https://web-km.tistory.com/42
     * */
    @Test
    fun updatedAtTest() {
        val savedBrand = brandRepository.save(testBrand)
        ReflectionTestUtils.setField(savedBrand, "name", "Chanel2")
        entityManager.flush()
        entityManager.clear()

        val updatedBrand = brandRepository.findAll()[0]
        assertTrue(updatedBrand.createdAt.isBefore(updatedBrand.updatedAt))
    }

}