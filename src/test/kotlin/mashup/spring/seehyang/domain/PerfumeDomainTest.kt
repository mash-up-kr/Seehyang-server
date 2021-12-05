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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
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

    @Test
    fun getPerfumesTest(){
        val perfume = perfumeRepository.findAll()[0]
        val perfumeId = perfume.id!!
        val brand = brandRepository.findAll()[0]
        val perfume2 = perfumeRepository.save(createTestPerfume(brand))
        //순서 조심
        entityManager.flush()
        entityManager.clear()

        val foundPerfume = perfumeDomain.getPerfumes(listOf(perfumeId,perfume2.id!!)).map { it.id!! }

        assertThat(foundPerfume).contains(perfumeId, perfume2.id!!)
    }

    @Test
    fun getPerfumeWithUserTest(){
        val perfume = perfumeRepository.findAll()[0]
        val user = userRepository.findAll()[0]
        perfume.likePerfume(user)

        val foundPerfume = perfumeDomain.getPerfumeWithUser(perfumeId = perfume.id!!, user)

        assertThat(foundPerfume.id).isEqualTo(perfume.id!!)
        assertThat(foundPerfume.isLiked).isTrue
        assertThat(foundPerfume.likeCount).isEqualTo(1)

    }

    // TODO : 나머지 테스트 만들기

}