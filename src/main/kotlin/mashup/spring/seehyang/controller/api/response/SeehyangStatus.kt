package mashup.spring.seehyang.controller.api.response

enum class SeehyangStatus(val code: Int) {
    OK(200),
    BAD_REQUEST(400),
    INTERNAL_SERVER_ERROR(500)
}