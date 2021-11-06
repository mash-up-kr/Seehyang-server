package mashup.spring.seehyang.exception

import mashup.spring.seehyang.controller.api.response.SeehyangStatus

/**
 * 클라이언트 측 오류
 * Api의 입력값이 잘못되었을 때 등
 */
class BadRequestException(status: SeehyangStatus
): BaseException(status.code, status.message)

/**
 * 인증 예외처리
 * 인증이 필요한 Api 를 인증 헤더 없이 호출하거나
 * 서비스 레벨에서 id 대조 실패했을 때 등
 */
class UnauthorizedException(status: SeehyangStatus
): BaseException(status.code, status.message)

/**
 * 입력 포맷은 정상적이나
 * 존재하지 않은 Id에 대해 호출하는 등
 */
class NotFoundException(status: SeehyangStatus
): BaseException(status.code, status.message)

/**
 * 서버 로직 에러
 */
class InternalServerException(status: SeehyangStatus
): BaseException(status.code, status.message)