package kr.respectme.common.advice

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@AutoConfiguration
class AdviceConfig(private val objectMapper: ObjectMapper) {

    @Bean
    fun generalExceptionHandler(): GeneralExceptionHandlerAdvice {
        return GeneralExceptionHandlerAdvice(objectMapper)
    }

    @Bean
    fun envelopPatternResponseBodyAdvice(): EnvelopPatternResponseBodyAdvice {
        return EnvelopPatternResponseBodyAdvice()
    }

    @Bean
    fun cursorPaginationResponseBodyAdvice(): CursorPaginationAdvice {
        return CursorPaginationAdvice()
    }
}