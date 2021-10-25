package mashup.spring.seehyang.exception

import mashup.spring.seehyang.controller.api.response.SeehyangStatus

class BadRequestException(status: SeehyangStatus
): BaseException(status.code, status.message)

class UnauthorizedException(status: SeehyangStatus
): BaseException(status.code, status.message)

class NotFoundException(status: SeehyangStatus
): BaseException(status.code, status.message)