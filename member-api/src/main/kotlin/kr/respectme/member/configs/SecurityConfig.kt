package kr.respectme.member.configs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.security.handler.EntrypointUnauthorizedHandler
import kr.respectme.common.security.handler.JwtAccessDeniedHandler
import kr.respectme.common.security.jwt.JwtAuthenticationFilter
import kr.respectme.common.security.jwt.JwtAuthenticationProvider
import kr.respectme.common.security.jwt.adapter.RestJwtAuthenticationAdapter
import kr.respectme.common.security.jwt.port.JwtAuthenticationPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${respect-me.msa.auth-api.url}")
    private val authUrl: String,
    private val objectMapper: ObjectMapper,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun jwtAuthenticationPort(): JwtAuthenticationPort {
        return RestJwtAuthenticationAdapter("${authUrl}/api/v1/auth/jwt/verify")
    }

    @Bean
    fun jwtAuthenticationProvider(): JwtAuthenticationProvider {
        return JwtAuthenticationProvider(jwtAuthenticationPort(), objectMapper)
    }

    fun excludePathMatcher(): RequestMatcher {
        val pathMatchers = listOf(
            AntPathRequestMatcher("/api/v1/members", "POST"),
            AntPathRequestMatcher("/static/**"),
            AntPathRequestMatcher("/swagger-ui/**"),
            AntPathRequestMatcher("/v3/**"),
            AntPathRequestMatcher("/api-docs/**"),
        )
        return OrRequestMatcher(*pathMatchers.toTypedArray())
    }

    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(excludePathMatcher(), jwtAuthenticationProvider(), objectMapper)
    }
//
    @Bean
    fun unauthorizedEntryPoint(): EntrypointUnauthorizedHandler {
        return EntrypointUnauthorizedHandler()
    }

    @Bean
    fun accessDeniedHandler(): JwtAccessDeniedHandler {
        return JwtAccessDeniedHandler()
    }

    @Bean
    fun httpSecurity(http: HttpSecurity): SecurityFilterChain {
        logger.debug("httpSecurity called.")
        return http.httpBasic { it.disable() }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling{ it ->
                it.accessDeniedHandler(accessDeniedHandler())
                it.authenticationEntryPoint(unauthorizedEntryPoint())
            }
            .authorizeHttpRequests {
                it.requestMatchers("/internal/api/v1/members/**").hasRole("SERVICE")
                it.anyRequest().permitAll()
            }
            .build()
    }
}