package kr.respectme.group.common.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.respectme.common.error.exporter.ErrorExporter
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
@Profile("test")
class LoggingFilter(
    private val messageExporter: ErrorExporter
) : OncePerRequestFilter() {

    private val pathMatcher = AntPathMatcher()
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!pathMatcher.match("/api/**", request.requestURI)) {
            logger.debug("request uri mismatched. ${request.requestURI}")
            filterChain.doFilter(request, response)
            return
        }

        logger.debug("request&response wrapped. ${request.requestURI}")
        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse)
        } finally {
            logRequestAndResponse(wrappedRequest, wrappedResponse)
            wrappedResponse.copyBodyToResponse()
        }
    }

    private fun logRequestAndResponse(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
    ) {
        logger.debug("log will be sent to discord")
        if(response.status >= 400) {
            messageExporter.export(request, response)
        }
    }
}