package mashup.spring.seehyang.config.interceptor

import mashup.spring.seehyang.controller.api.Authenticated
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.service.UserJwtService
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationInterceptor(
    private val userJwtService: UserJwtService,
): HandlerInterceptor{

    override fun preHandle(request: HttpServletRequest,
                           response: HttpServletResponse,
                           handler: Any
    ): Boolean {
        if(isNeedAuthentication(handler)) {
            request.setAttribute("userId", authenticate(request).id)
        }
        return true
    }

    /**
     * 인증이 필요한 요청 확인
     */
    private fun isNeedAuthentication(handler: Any): Boolean =
        if(handler is HandlerMethod) {
            handler.getMethodAnnotation(Authenticated::class.java) != null ||
                    handler.beanType.getAnnotation(Authenticated::class.java) != null
        } else {
            false
        }

    /**
     * 해당 인증이 올바른지 체크
     */
    private fun authenticate(req: HttpServletRequest): UserDto {
        val token = req.getHeader("Authorization")
            ?: throw RuntimeException("Not allow user")
        return userJwtService.decode(token)
    }
}