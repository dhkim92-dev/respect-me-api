package kr.respectme.common.security.jwt

import feign.Feign
import kr.respectme.common.annotation.ServiceAccount
import kr.respectme.common.security.jwt.adapter.JwtAuthenticationAdapter
import kr.respectme.common.security.jwt.adapter.JwtAuthenticationRequirementsRequestAdapter
import kr.respectme.common.security.jwt.port.JwtAuthenticationPort
import kr.respectme.common.security.jwt.port.JwtAuthenticationRequirementsRequestPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder

@Configuration
@AutoConfiguration
@ConditionalOnProperty(prefix = "respectme.security.jwt", name = ["enabled"], havingValue = "true", matchIfMissing = false)
@EnableFeignClients(clients=[JwtAuthenticationRequirementsRequestAdapter::class])
class DefaultJwtAuthenticationConfig(
    @Value("\${respect-me.msa.auth-api.url}")
    private val authApiUrl: String,
    @Value("\${respect-me.service-account.token}")
    private val serviceToken: String
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.debug("DefaultJwtAuthenticationConfig initialized.")
    }

    @Bean
    fun jwtAuthenticationPort(jwtAuthenticationRequirementsRequestPort: JwtAuthenticationRequirementsRequestPort): JwtAuthenticationPort {
        return JwtAuthenticationAdapter(serviceToken = "Bearer ${serviceToken}", jwtAuthenticationRequirementsRequestPort = jwtAuthenticationRequirementsRequestPort)
    }

    @Bean
    fun jwtAuthenticationProvider(jwtAuthenticationPort: JwtAuthenticationPort): JwtAuthenticationProvider {
        return JwtAuthenticationProvider(jwtAuthenticationPort)
    }
}