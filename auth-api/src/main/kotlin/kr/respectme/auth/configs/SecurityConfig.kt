package kr.respectme.auth.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig() {

    @Bean
    fun httpSecurity(http: HttpSecurity): SecurityFilterChain {
        http.httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests{ it.anyRequest().permitAll() }
        return http.build()
    }
}