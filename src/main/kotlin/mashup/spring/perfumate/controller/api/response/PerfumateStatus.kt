package mashup.spring.perfumate.controller.api.response

enum class PerfumateStatus(val code: Int) {
    OK(200),
    BAD_REQUEST(400),
    INTERNAL_SERVER_ERROR(500)
}