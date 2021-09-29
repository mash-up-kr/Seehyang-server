package mashup.spring.perfumate.controller.api.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PerfumateResponse<T>(
    val data: T? = null,
    val status: PerfumateStatus = PerfumateStatus.OK,
    val message: String? = null
)