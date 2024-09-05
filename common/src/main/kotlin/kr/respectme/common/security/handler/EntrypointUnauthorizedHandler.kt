package kr.respectme.common.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.respectme.common.error.GlobalErrorCode
import kr.respectme.common.response.ErrorResponse
import kr.respectme.common.security.jwt.JwtAuthenticationException
import org.slf4j.LoggerFactory
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.core.AuthenticationException

class EntrypointUnauthorizedHandler(): AuthenticationEntryPoint {

    private val logger = LoggerFactory.getLogger(javaClass)
//    private val objectMapper = ObjectMapper()

    companion object {
        private val DEFAULT_UNAUTHORIZED_EXCEPTION = ObjectMapper().writeValueAsString(
            ErrorResponse.of(GlobalErrorCode.UNAUTHORIZED_EXCEPTION)
        )
    }

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.error("Unauthorized Error, ${authException.message}")
        val message = when(authException) {
            is JwtAuthenticationException -> authException.message
            else -> DEFAULT_UNAUTHORIZED_EXCEPTION
        }
        response.contentType = "application/json;charset=UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer.write(message)
        response.writer.flush()
        response.writer.close()
    }
}