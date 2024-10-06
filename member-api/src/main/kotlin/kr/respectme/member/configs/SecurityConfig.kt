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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${respect-me.msa.auth-api.url}")
    private val authUrl: String,
    private val objectMapper: ObjectMapper,
    @Value("\${server.cors.allowed-origins}")
    private val allowedOriginsString: String
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val allowedOrigins: List<String> by lazy {
        allowedOriginsString.split(",")
    }

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
            AntPathRequestMatcher("/members/swagger-ui.html"),
            AntPathRequestMatcher("/members/v3/api-docs"),
//            AntPathRequestMatcher("/v3/**"),
//            AntPathRequestMatcher("/api-docs/**"),
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
    fun corsConfig(): CorsConfiguration {
        val config = CorsConfiguration()
        config.allowedOrigins = allowedOrigins
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        config.allowedHeaders = listOf("*")
        return config
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig())
        return source
    }

    @Bean
    @ConditionalOnProperty(name = ["spring.h2.console.enabled"], havingValue = "true")
    fun ignoringWebSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it -> it.ignoring()
            .requestMatchers(PathRequest.toH2Console())
        }
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
                it.requestMatchers("/members/swagger-ui.html", "/members/v3/api-docs", "/members/swagger-ui/**", "/members/v3/api-docs/**").permitAll()
                it.requestMatchers("/internal/api/v1/members/**").hasRole("SERVICE")
                it.requestMatchers("/api/v1/members", "POST").permitAll()
                it.requestMatchers("/api/v1/**").hasAnyRole("MEMBER", "ADMIN", "SERVICE")
                it.anyRequest().permitAll()
            }
            .build()
    }
}