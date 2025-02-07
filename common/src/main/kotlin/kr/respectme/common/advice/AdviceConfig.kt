package kr.respectme.common.advice

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order


@Configuration
@AutoConfiguration
class AdviceConfig(private val objectMapper: ObjectMapper) {

    @Bean
    fun generalExceptionHandler(): GeneralExceptionHandlerAdvice {
        return GeneralExceptionHandlerAdvice(objectMapper)
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun envelopPatternResponseBodyAdvice(): EnvelopPatternResponseBodyAdvice {
        return EnvelopPatternResponseBodyAdvice()
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    fun cursorPaginationResponseBodyAdvice(): CursorPaginationAdvice {
        return CursorPaginationAdvice()
    }
}