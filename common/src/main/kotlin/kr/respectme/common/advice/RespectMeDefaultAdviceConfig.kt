package kr.respectme.common.advice

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@AutoConfiguration
@Configuration
class RespectMeDefaultAdviceConfig(private val objectMapper: ObjectMapper) {
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