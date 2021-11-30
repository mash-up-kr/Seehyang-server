package mashup.spring.seehyang.controller.api

import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.repository.ImageRepository
import mashup.spring.seehyang.service.AwsS3UploadService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile


@ApiV1
class ImageUploadController(
    private val awsS3UploadService: AwsS3UploadService,
    private val imageRepository: ImageRepository
) {
    @PostMapping("/image")
    fun upload(@RequestParam("image") multipartFile: MultipartFile): SeehyangResponse<Map<String, Long>> {
        //TODO: 에러 핸들링 imageUrl NPE?
        //TODO: repository 수정 필요
        val imageUrl = awsS3UploadService.upload(multipartFile, "application/image/post")
        val image = Image(url = imageUrl!!)
        imageRepository.save(image)
        return SeehyangResponse(mutableMapOf(Pair("imageId", image.id!!)))
    }
}