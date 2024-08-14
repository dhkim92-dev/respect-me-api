package kr.respectme.common.resolvers

import kr.respectme.common.annotation.LoginMember
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.GlobalErrorCode
import kr.respectme.common.error.UnauthorizedException
import kr.respectme.common.security.jwt.JwtAuthentication
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*

class LoginMemberResolver: HandlerMethodArgumentResolver {

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): UUID? {
        val required = parameter.getParameterAnnotation(LoginMember::class.java)!!.required
        val principal = SecurityContextHolder.getContext().authentication?.principal as? JwtAuthentication

        return if (required) {
            principal?.id ?: throw UnauthorizedException(GlobalErrorCode.UNAUTHORIZED_EXCEPTION)
        } else {
            principal?.id
        }
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginMember::class.java)
    }
}
