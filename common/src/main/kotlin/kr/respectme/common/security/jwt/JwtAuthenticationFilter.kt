package kr.respectme.common.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import feign.FeignException.FeignClientException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.respectme.common.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val exclusiveRequestMatcher: RequestMatcher,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val objectMapper: ObjectMapper
): OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.debug("JwtAuthenticationFilter generated.")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if(exclusiveRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response)
            return
        }

        val token = resolveAuthorizationHeader(request)

        token?.let{
            try {
                val jwtAuthentication = jwtAuthenticationProvider.authenticate(JwtAuthenticationToken(token))
                jwtAuthentication?.let { SecurityContextHolder.getContext().authentication = jwtAuthentication }
            } catch(e: FeignClientException) {
                logger.error("FeignClientException, ${e.message}")
                when(e.status()) {
                    401 -> {
                       val body = e.contentUTF8()
                        if (canMapStringToErrorResponse(body)) {
                            throw JwtAuthenticationException(body)
                        } else {
                            logger.error(body)
                            throw JwtAuthenticationException()
                        }
                    }
                    else -> {
                        logger.error(e.message)
                        throw e
                    }
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveAuthorizationHeader(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")?.takeIf { token
            -> token.startsWith("Bearer ")
        }?.let{
            it.substring(7)
        }
    }

    private fun canMapStringToErrorResponse(body: String): Boolean {
        return try {
            objectMapper.readValue(body, ErrorResponse::class.java)
            true
        } catch(e: Exception) {
            false
        }
    }
}