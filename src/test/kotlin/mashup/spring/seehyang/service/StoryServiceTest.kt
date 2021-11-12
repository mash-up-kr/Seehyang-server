package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.createTestBrand
import mashup.spring.seehyang.createTestImage
import mashup.spring.seehyang.createTestPerfume
import mashup.spring.seehyang.createTestUser
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.perfume.Brand
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.StoryLikeRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.community.StoryTagRepository
import mashup.spring.seehyang.repository.community.TagRepository
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class StoryServiceTest  @Autowired constructor(
    val storyLikeRepository: StoryLikeRepository,
    val userRepository: UserRepository,
    val imageRepository: ImageRepository,
    val brandRepository: BrandRepository,
    val storyRepository: StoryRepository,
    val perfumeRepository: PerfumeRepository,
    val storyTagRepository: StoryTagRepository,
    val tagRepository: TagRepository
) {

    private val expectedTag: String ="태그1"
    private val expectedTag2: String ="태그2"

    private lateinit var user: User
    private lateinit var brand: Brand
    private lateinit var perfume: Perfume
    private lateinit var image: Image

    private val tagService : TagService = TagService(tagRepository, storyTagRepository)
    private val storyService: StoryService = StoryService(storyLikeRepository, storyRepository, imageRepository, perfumeRepository, tagService,userRepository)
    @BeforeEach
    fun setUp() {
        user = userRepository.save(createTestUser())
        brand = brandRepository.save(createTestBrand())
        perfume = perfumeRepository.save(createTestPerfume(brand))
        image = imageRepository.save(createTestImage())
    }

    @Test
    fun storyCreateTest() {
        val request = StoryCreateRequest(perfume.id!!, image.id!!, mutableListOf(expectedTag, expectedTag2))

        val actual = storyService.create(user, request)
        
        assertEquals(expectedTag, actual.tags[0].contents)
        assertEquals(expectedTag2, actual.tags[1].contents)
    }

}