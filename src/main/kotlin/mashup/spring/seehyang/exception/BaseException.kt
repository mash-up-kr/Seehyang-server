package mashup.spring.seehyang.exception

open class BaseException(
    val code: Int,
    override val message: String
): RuntimeException()