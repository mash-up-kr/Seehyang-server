package mashup.spring.seehyang.config.resolver

import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.service.UserJwtService
import mashup.spring.seehyang.service.UserService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserArgumentResolver(
    private val userJwtService: UserJwtService,
    private val userService: UserService,
): HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(Logined::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val token = webRequest.getHeader("Authorization") ?: return User()
        if(token.isBlank()) return User()
        return userService.getUser(userJwtService.decode(token))
    }
}