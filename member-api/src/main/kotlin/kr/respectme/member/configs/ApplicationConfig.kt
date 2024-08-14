package kr.respectme.member.configs

import kr.respectme.common.resolvers.LoginMemberResolver
import kr.respectme.common.resolvers.ServiceAccountResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ApplicationConfig: WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>)
    {
        resolvers.add(LoginMemberResolver())
        resolvers.add(ServiceAccountResolver())
        super.addArgumentResolvers(resolvers)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(10)
    }
}