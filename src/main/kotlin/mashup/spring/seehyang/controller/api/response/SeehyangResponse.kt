package mashup.spring.seehyang.controller.api.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SeehyangResponse<T>(
    val data: T? = null,
    val status: SeehyangStatus = SeehyangStatus.OK,
    val message: String? = null
)