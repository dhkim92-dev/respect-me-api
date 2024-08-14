package kr.respectme.common.resolvers

import kr.respectme.common.annotation.ServiceAccount
import kr.respectme.common.error.GlobalErrorCode
import kr.respectme.common.error.UnauthorizedException
import kr.respectme.common.security.jwt.JwtAuthentication
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.UUID

class ServiceAccountResolver: HandlerMethodArgumentResolver {

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): UUID? {
        val principal = SecurityContextHolder.getContext().authentication
            ?.principal
            ?.takeIf { it ->
                it is JwtAuthentication
            } as JwtAuthentication? ?: throw UnauthorizedException(GlobalErrorCode.UNSUPPORTED_PRINCIPAL_EXCEPTION)

        principal.roles.forEach {
            if(it.authority == "ROLE_SERVICE") {
                return principal.id
            }
        }

        throw UnauthorizedException(GlobalErrorCode.REQUIRE_SERVICE_ACCOUNT_EXCEPTION)
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(ServiceAccount::class.java)
    }
}