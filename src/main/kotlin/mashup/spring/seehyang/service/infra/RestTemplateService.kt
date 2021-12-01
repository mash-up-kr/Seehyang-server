package mashup.spring.seehyang.service.infra

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class RestTemplateService(
    private val restTemplate: RestTemplate,
) {
    // get
    fun <T> get(url: String, httpHeaders: HttpHeaders, clazz: Class<T>,
    ): ResponseEntity<T> =
        call(url, HttpMethod.GET, httpHeaders, null, clazz)

    // post
    fun <T> post(url: String, httpHeaders: HttpHeaders, body: Any, clazz: Class<T>,
    ): ResponseEntity<T> =
        call(url, HttpMethod.POST, httpHeaders, body, clazz)

    private fun <T> call(url: String, httpMethod: HttpMethod, httpHeaders: HttpHeaders, body: Any?, clazz: Class<T>
    ): ResponseEntity<T> =
        restTemplate.exchange(url, httpMethod, HttpEntity(body, httpHeaders), clazz)
}