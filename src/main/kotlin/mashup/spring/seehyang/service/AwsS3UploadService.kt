package mashup.spring.seehyang.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

//TODO : 복붙 코드라 리팩토링 필요. (에러 핸들링 등)
@Service
class AwsS3UploadService(
    private val awsS3Client: AmazonS3Client
) {

    @Value("\${cloud.aws.s3.bucket}")
    var bucket: String? = null

    @Throws(IOException::class)
    fun upload(multipartFile: MultipartFile, dirName: String): String? {
        val uploadFile: File? = convert(multipartFile)
        return upload(uploadFile, dirName)
    }

    // S3로 파일 업로드하기
    private fun upload(uploadFile: File?, dirName: String): String? {
        val fileName = dirName + "/" + UUID.randomUUID() + uploadFile?.name // S3에 저장된 파일 이름
        val uploadImageUrl = uploadFile?.let { putS3(it, fileName) } // s3로 업로드
        uploadFile?.let { removeNewFile(it) }
        return fileName
    }

    // S3로 업로드
    private fun putS3(uploadFile: File, fileName: String): String {
        awsS3Client.putObject(
            PutObjectRequest(
                bucket,
                fileName,
                uploadFile
            ).withCannedAcl(CannedAccessControlList.PublicRead)
        )
        return awsS3Client.getUrl(bucket, fileName).toString()
    }

    // 로컬에 저장된 이미지 지우기
    private fun removeNewFile(targetFile: File) {
        targetFile.delete()
    }

    // 로컬에 파일 업로드 하기
    @Throws(IOException::class)
    private fun convert(file: MultipartFile): File? {
        val convertFile = File(System.getProperty("user.dir") + "/" + file.originalFilename)
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            FileOutputStream(convertFile).use { fos ->  // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.bytes)
            }
            return convertFile
        }
        return null
    }
}