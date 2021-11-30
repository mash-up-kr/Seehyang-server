package mashup.spring.seehyang.config.resolver

import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.controller.api.response.SeehyangStatus
import mashup.spring.seehyang.domain.entity.user.User
import mashup.spring.seehyang.exception.InternalServerException
import mashup.spring.seehyang.repository.user.UserRepository
import mashup.spring.seehyang.service.UserJwtService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserArgumentResolver(
    private val userJwtService: UserJwtService,
    private val userRepository: UserRepository
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
        if(token.isNullOrBlank()) return UserDto(User.empty())
        val userId = userJwtService.getUserIdByToken(token)

        return UserDto(userRepository.findById(userId).orElseThrow{InternalServerException(SeehyangStatus.INTERNAL_SERVER_ERROR)})
    }
}