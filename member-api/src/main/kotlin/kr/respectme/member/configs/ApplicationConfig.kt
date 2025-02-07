package kr.respectme.member.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import kr.respectme.common.advice.CursorPaginationAdvice
import kr.respectme.common.advice.EnvelopPatternResponseBodyAdvice
import kr.respectme.common.advice.GeneralExceptionHandlerAdvice
import kr.respectme.common.resolvers.LoginMemberResolver
import kr.respectme.common.resolvers.ServiceAccountResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ApplicationConfig(
    private val objectMapper: ObjectMapper
): WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(LoginMemberResolver())
        resolvers.add(ServiceAccountResolver())
        super.addArgumentResolvers(resolvers)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(10)
    }

//    @Bean
//    fun envelopPatternResponseBodyAdvice(): EnvelopPatternResponseBodyAdvice {
//        return EnvelopPatternResponseBodyAdvice()
//    }
//
//    @Bean
//    fun cursorPaginationAdvice(): CursorPaginationAdvice {
//        return CursorPaginationAdvice()
//    }
//
//    @Bean
//    fun generalExceptionHandler(): GeneralExceptionHandlerAdvice {
//        return GeneralExceptionHandlerAdvice(objectMapper)
//    }
}