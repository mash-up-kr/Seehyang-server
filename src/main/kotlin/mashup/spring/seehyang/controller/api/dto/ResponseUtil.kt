package mashup.spring.seehyang.controller.api.dto

import org.springframework.beans.factory.annotation.Value

@Value("\${cloud.aws.s3.bucket}")
var s3Url: String? = null

fun addBucketUrl(url: String) = s3Url + url
