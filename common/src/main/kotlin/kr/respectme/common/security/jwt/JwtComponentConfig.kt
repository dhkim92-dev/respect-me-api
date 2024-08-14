package kr.respectme.common.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.security.jwt.port.JwtAuthenticationPort
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtComponentConfig(
    private val objectMapper: ObjectMapper,
    private val jwtAuthenticationPort: JwtAuthenticationPort,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.debug("JwtComponentConfig generated.")
    }

    @Bean
    fun jwtAuthenticationProvider(): JwtAuthenticationProvider {
        return JwtAuthenticationProvider(jwtAuthenticationPort, objectMapper)
    }
}