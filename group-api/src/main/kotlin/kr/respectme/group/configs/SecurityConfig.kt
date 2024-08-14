package kr.respectme.group.configs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.security.jwt.JwtAuthenticationFilter
import kr.respectme.common.security.jwt.JwtAuthenticationProvider
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun excludePathMatcher(): RequestMatcher {
        val matchers = AntPathRequestMatcher("/api/v1/**")
        return OrRequestMatcher(matchers)
    }

    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(excludePathMatcher(), jwtAuthenticationProvider, objectMapper)
    }

    @Bean
    @Order(1)
    fun httpSecurity(http: HttpSecurity): SecurityFilterChain {
        logger.info("httpSecurity(http) called.")
        http.httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests{ it.anyRequest().permitAll() }
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}