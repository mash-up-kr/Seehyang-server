package mashup.spring.seehyang.config.resolver

import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.service.auth.UserJwtService
import mashup.spring.seehyang.service.auth.UserDetailsService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserArgumentResolver(
    private val userJwtService: UserJwtService,
    private val userDetailsService: UserDetailsService
): HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        if(parameter.parameterType == UserDto::class.java) return true
        return false
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {

        val token = webRequest.getHeader("Authorization")

        if(token.isNullOrBlank()) return null

        val userIdByToken = userJwtService.getUserIdByToken(token)
        val userIdFromDB = userDetailsService.getUserDtoByUserId(userIdByToken)

        return userIdFromDB
    }
}