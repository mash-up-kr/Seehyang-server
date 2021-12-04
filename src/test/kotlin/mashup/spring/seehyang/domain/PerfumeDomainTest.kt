package mashup.spring.seehyang.domain

import mashup.spring.seehyang.createTestBrand
import mashup.spring.seehyang.createTestPerfume
import mashup.spring.seehyang.createTestUser
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeLikeRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager

@DataJpaTest
class PerfumeDomainTest @Autowired constructor(
    val perfumeRepository: PerfumeRepository,
    val perfumeLikeRepository: PerfumeLikeRepository,
    val imageRepository: ImageRepository,
    val userRepository: UserRepository,
    val storyRepository: StoryRepository,
    val brandRepository: BrandRepository,
    val entityManager: EntityManager,

) {

    val perfumeDomain = PerfumeDomain(perfumeRepository,perfumeLikeRepository)

    @BeforeEach
    fun before(){
        //set other entities
        val image = Image(url = "test")
        val brand = createTestBrand()
        val perfume = createTestPerfume(brand)
        val user = createTestUser(true)

        imageRepository.save(image)
        brandRepository.save(brand)
        perfumeRepository.save(perfume)
        userRepository.save(user)
    }

    @Test
    fun getPerfumeTest(){
        val perfume = perfumeRepository.findAll()[0]
        val perfumeId = perfume.id!!
        //순서 조심
        entityManager.flush()
        entityManager.clear()

        val foundPerfume = perfumeDomain.getPerfume(perfumeId)

        assertThat(foundPerfume.id).isEqualTo(perfume.id)
    }



}