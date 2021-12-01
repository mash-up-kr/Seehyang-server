package mashup.spring.seehyang.service

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.controller.api.dto.perfume.BrandDto
import mashup.spring.seehyang.controller.api.dto.perfume.PerfumeDto
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.StoryDomain
import mashup.spring.seehyang.domain.UserDomain
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.Story
import mashup.spring.seehyang.domain.entity.perfume.Perfume
import mashup.spring.seehyang.exception.NotFoundException
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.perfume.BrandRepository
import mashup.spring.seehyang.repository.perfume.PerfumeRepository
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.service.infra.AwsS3UploadService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Transactional
@Service
class AdminService(
    val storyRepository: StoryRepository,
    val awsS3UploadService: AwsS3UploadService,
    val imageRepository: ImageRepository,
    val brandRepository: BrandRepository,
    val perfumeRepository: PerfumeRepository,
    private val storyService: StoryService,
    private val userRepository: UserRepository

) {

    /**
     * ========= Story =============
     */
    @Transactional(readOnly = true)
    fun getAdminStoryDetail(storyId: Long): StoryDto {

        val story = storyRepository.findById(storyId).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_STORY) }

        return StoryDto(story)
    }
    @Transactional(readOnly = true)
    fun getStories(pageRequest: PageRequest): List<StoryDto>{
        return storyRepository.findAll(pageRequest).map { StoryDto(it) }.toList()
    }
    @Transactional(readOnly = true)
    fun getStoryCount(): Long{
        return storyRepository.count()
    }

    fun saveStory(perfumeId: Long, tags: String, image: MultipartFile,){
        val uploadedImage = awsS3UploadService.upload(image, "application/image/post")
        val image = Image(url = uploadedImage!!)
        imageRepository.save(image)
        val user = userRepository.findById(9L).get()

        storyService.createStory(UserDto(user), StoryCreateRequest(perfumeId, image.id!!, tags.split(","), isOnlyMe = false))
    }

    /**
     * ========== Brand ============
     */
    @Transactional(readOnly = true)
    fun getBrands(pageRequest: PageRequest): List<BrandDto>{
        return brandRepository.findAll(pageRequest).map { BrandDto(it) }.toList()
    }
    @Transactional(readOnly = true)
    fun getBrandCount(): Long{
        return brandRepository.count()
    }
    @Transactional(readOnly = true)
    fun getBrand(brandId: Long): BrandDto{
        val brand = brandRepository.findById(brandId).orElseThrow { NotFoundException(SeehyangStatus.NOT_FOUND_BRNAD) }
        return BrandDto(brand)
    }


    /**
     * ========== Perfume ==========
     */
    @Transactional(readOnly = true)
    fun getPerfumes(pageRequest: PageRequest): List<PerfumeDto>{
        return perfumeRepository.findAll(pageRequest).map { PerfumeDto(it) }.toList()
    }
    @Transactional(readOnly = true)
    fun getPerfume(perfumeId: Long): PerfumeDto{
        return perfumeRepository.findById(perfumeId).map { PerfumeDto(it) }.orElseThrow{NotFoundException(SeehyangStatus.NOT_FOUND_PERFUME)}
    }
    @Transactional(readOnly = true)
    fun getPerfumeCount(): Long{
        return perfumeRepository.count()
    }


}