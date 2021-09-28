package mashup.spring.perfumate.controller.api

import mashup.spring.perfumate.service.AwsS3UploadService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.IOException


@ApiV1
class ImageUploadController(
    private val awsS3UploadService: AwsS3UploadService
) {
    @PostMapping("/image")
    @Throws(IOException::class)
    fun upload(@RequestParam("images") multipartFile: MultipartFile): String? {
        return awsS3UploadService.upload(multipartFile, "application/post")
    }
}