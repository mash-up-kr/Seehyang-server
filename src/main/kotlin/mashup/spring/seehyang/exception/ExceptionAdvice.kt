package mashup.spring.seehyang.exception

import mashup.spring.seehyang.controller.api.response.SeehyangResponse
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionAdvice {
    private val logger: Logger = LoggerFactory.getLogger("Seehyang Exception Advice")

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(
        exception: BaseException
    ): SeehyangResponse<Unit> {
        logger.error(exception.message)
        val curException = SeehyangStatus.findByCode(exception.code)
        return SeehyangResponse(
            data = null,
            code = curException.code,
            status = curException,
            message = exception.message,
        )
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleUnexpectedException(
        exception: RuntimeException
    ): SeehyangResponse<Unit> {
        logger.error(exception.message)
        return SeehyangResponse(
            data = null,
            code = SeehyangStatus.INTERNAL_SERVER_ERROR.code,
            status = SeehyangStatus.INTERNAL_SERVER_ERROR,
            message = exception.message
        )
    }
}