package mashup.spring.seehyang.controller.admin

import mashup.spring.seehyang.controller.api.dto.community.StoryCreateRequest
import mashup.spring.seehyang.controller.api.dto.community.StoryDto
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.repository.community.StoryRepository
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.service.AwsS3UploadService
import mashup.spring.seehyang.service.StoryService
import org.springframework.data.domain.PageRequest
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@AdminController
class AdminStoryController(
    val awsS3UploadService: AwsS3UploadService,
    val imageRepository: ImageRepository,
    private val storyService: StoryService,
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository // 테스트를 위한 임시 작업.
) {

    @GetMapping("/entity/Story")
    fun adminStory(
        model : Model,
        @RequestParam(value = "page", defaultValue = "1") page : Int
    ): String {
        val pageRequest = PageRequest.of(page - 1, 30)
        val items = storyRepository.findAll(pageRequest).content
        val count = storyRepository.count()

        val maxPage = if (count%30L==0L) count/30 else count/30 + 1

        model.addAttribute("items", items)
        model.addAttribute("currentPage", page)
        model.addAttribute("maxPage", maxPage)
        return "story/storyList"
    }

    @GetMapping("/entity/Story/{id}")
    fun adminStoryDetail(
        model: Model,
        @PathVariable id: Long,
        @RequestParam(value = "page", defaultValue = "1") page : Int
    ) : String{
        val story = storyService.getStoryDetail(id)
        model.addAttribute("item", StoryDto(story))
        model.addAttribute("prevPage", page)
        return "story/storyDetail"
    }

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