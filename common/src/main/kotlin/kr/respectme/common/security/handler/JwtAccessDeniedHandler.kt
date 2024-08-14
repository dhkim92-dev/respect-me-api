package kr.respectme.common.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.respectme.common.error.GlobalErrorCode
import kr.respectme.common.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler

class JwtAccessDeniedHandler: AccessDeniedHandler{

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        private val DEFAULT_ACCESS_DENIED = ObjectMapper().writeValueAsString(
            ErrorResponse.of(GlobalErrorCode.ACCESS_DENIED_EXCEPTION)
        )
    }

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        logger.error("Access Denied, ${accessDeniedException?.message}")
        response?.status = HttpServletResponse.SC_FORBIDDEN
        response?.contentType = "application/json; charset=utf-8"
        response?.writer?.write(DEFAULT_ACCESS_DENIED)
        response?.writer?.flush()
        response?.writer?.close()
    }
}