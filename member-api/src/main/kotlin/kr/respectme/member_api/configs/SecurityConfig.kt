package kr.respectme.member_api.configs

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
    private val WHITELIST_STATIC = arrayOf("/static/css/**", "/static/js/**", "*.ico", "/error")
    private val WHITELIST_SWAGGER = arrayOf("/swagger-ui", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs", "/api-docs", "/api-docs/**")
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring().requestMatchers(*WHITELIST_STATIC, *WHITELIST_SWAGGER)
            it.ignoring().requestMatchers(PathRequest.toH2Console())
        }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        logger.info("SecurityFilterChain created.")
        http.csrf { csrf -> csrf.disable() }
            .cors { cors -> cors.disable() }
            .formLogin { form -> form.disable() }
            .httpBasic { basic -> basic.disable() }
            .sessionManagement {
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        return http.build();
    }
}