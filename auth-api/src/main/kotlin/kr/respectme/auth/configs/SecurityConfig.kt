package kr.respectme.auth.configs

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val objectMapper: ObjectMapper,
    @Value("\${server.cors.allowed-origins}")
    private val allowedOriginsString: String
) {

    private val allowedOrigins = allowedOriginsString.split(",")

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun corsConfig(): CorsConfiguration {
        val config = CorsConfiguration()
        //allowedOrigins.forEach { origin -> config.addAllowedOrigin(origin) }
        logger.info("allowedOrigins: ${allowedOrigins.joinToString(", ") }")
        config.allowedOrigins = allowedOrigins
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        config.allowedHeaders = listOf("*")
        config.allowCredentials = true
        return config
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig())
        return source
    }

    @Bean
    fun corsFilter(): CorsFilter {
        return CorsFilter(corsConfigurationSource())
    }

    @Bean
    fun httpSecurity(http: HttpSecurity): SecurityFilterChain {
        logger.debug("httpSecurity called.")
        http.httpBasic { it.disable() }
            .formLogin { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .authorizeHttpRequests{
                it.requestMatchers("/auth/swagger-ui.html", "/auth/v3/api-docs", "/auth/swagger-ui/**", "/auth/v3/api-docs/**").permitAll()
                it.anyRequest().permitAll()
            }
        return http.build()
    }
}