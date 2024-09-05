package kr.respectme.auth.configs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.advice.CursorPaginationAdvice
import kr.respectme.common.advice.EnvelopPatternResponseBodyAdvice
import kr.respectme.common.advice.GeneralExceptionHandlerAdvice
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ControllerAdvice

@Configuration
class ApplicationConfig(private val objectMapper: ObjectMapper) {

    @Bean
    fun exceptionHandler() : GeneralExceptionHandlerAdvice {
        return GeneralExceptionHandlerAdvice(objectMapper)
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun envelopResponseAdvice(): EnvelopPatternResponseBodyAdvice {
        return EnvelopPatternResponseBodyAdvice()
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    fun cursorPaginationAdvice(): CursorPaginationAdvice {
        return CursorPaginationAdvice()
    }
}