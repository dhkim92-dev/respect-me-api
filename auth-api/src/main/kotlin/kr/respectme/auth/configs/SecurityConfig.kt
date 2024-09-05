package kr.respectme.auth.configs

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class JMSecurityConfig(private val objectMapper: ObjectMapper) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun httpSecurity(http: HttpSecurity): SecurityFilterChain {
        logger.debug("httpSecurity called.")
        http.httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests{ it.anyRequest().permitAll() }
        return http.build()
    }
}