package kr.respectme.common.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.error.GlobalErrorCode
import kr.respectme.common.response.ErrorResponse
import org.springframework.security.core.AuthenticationException

class JwtAuthenticationException(
    message: String //= DEFAULT_UNAUTHORIZED_EXCEPTION
): AuthenticationException(message) {
//
//    companion object {
//        private val DEFAULT_UNAUTHORIZED_EXCEPTION = ObjectMapper().writeValueAsString(
//            ErrorResponse.of(GlobalErrorCode.UNAUTHORIZED_EXCEPTION)
//        )
//    }
}