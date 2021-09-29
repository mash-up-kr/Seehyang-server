package mashup.spring.perfumate.controller.api

import mashup.spring.perfumate.domain.entity.Image
import mashup.spring.perfumate.repository.ImageRepository
import mashup.spring.perfumate.service.AwsS3UploadService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.IOException


@ApiV1
class ImageUploadController(
    private val awsS3UploadService: AwsS3UploadService,
    private val imageRepository: ImageRepository
) {
    @PostMapping("/image")
    fun upload(@RequestParam("image") multipartFile: MultipartFile): Long {
        //TODO: 에러 핸들링 imageUrl NPE?
        val imageUrl = awsS3UploadService.upload(multipartFile, "application/post")
        val image = Image(url = imageUrl!!)
        imageRepository.save(image)
        return image.id!!
    }
}