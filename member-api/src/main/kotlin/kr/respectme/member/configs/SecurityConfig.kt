package kr.respectme.member.configs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.security.handler.EntrypointUnauthorizedHandler
import kr.respectme.common.security.handler.JwtAccessDeniedHandler
import kr.respectme.common.security.jwt.JwtAuthenticationFilter
import kr.respectme.common.security.jwt.JwtAuthenticationProvider
import org.slf4j.LoggerFactory
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
    private val objectMapper: ObjectMapper,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
//    private val WHITELIST_STATIC = arrayOf("/static/css/**", "/static/js/**", "*.ico", "/error")
//    private val WHITELIST_SWAGGER = arrayOf("/swagger-ui", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs", "/api-docs", "/api-docs/**")
    private val logger = LoggerFactory.getLogger(javaClass)

    fun excludePathMatcher(): RequestMatcher {
        val pathMatchers = listOf(
            AntPathRequestMatcher("/api/v1/members/sign-in", "POST"),
            AntPathRequestMatcher("/static/**"),
            AntPathRequestMatcher("/swagger-ui/**"),
            AntPathRequestMatcher("/v3/**"),
            AntPathRequestMatcher("/api-docs/**"),
        )
        return OrRequestMatcher(*pathMatchers.toTypedArray())
    }

    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(excludePathMatcher(), jwtAuthenticationProvider, objectMapper)
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