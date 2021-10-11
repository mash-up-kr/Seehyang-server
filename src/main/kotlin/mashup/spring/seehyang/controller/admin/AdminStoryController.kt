package mashup.spring.seehyang.controller.admin

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.service.AwsS3UploadService
import mashup.spring.seehyang.service.StoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@AdminController
class AdminStoryController(
    val awsS3UploadService: AwsS3UploadService,
    val imageRepository: ImageRepository,
    private val storyService: StoryService,
    private val userRepository: UserRepository // 테스트를 위한 임시 작업.
) {

    @GetMapping("story/upload")
    fun storyUploadPage(): String {
        return "story/upload"
    }

    @PostMapping("story/create.do")
    fun storyCreate(
        @RequestParam("perfumeId") perfumeId: Long,
        @RequestParam("tags") tags: String,
        @RequestParam("image") image: MultipartFile,
    ): String {
        val uploadedImage = awsS3UploadService.upload(image, "application/image/post")
        val image = Image(url = uploadedImage!!)
        imageRepository.save(image)
        val user = userRepository.findById(9L).get()

        storyService.create(user, StoryCreateRequest(perfumeId, image.id!!, tags.split(",")))
        return "story/upload"
    }
}