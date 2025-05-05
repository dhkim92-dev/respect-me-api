package kr.respectme.common.advice

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order


@Configuration
@AutoConfiguration
class AdviceConfig(
    private val objectMapper: ObjectMapper,
) {

    @Bean
    fun generalExceptionHandler(): GeneralExceptionHandlerAdvice {
        return GeneralExceptionHandlerAdvice(objectMapper)
    }

    @Bean
    @Order(3)
    fun envelopPatternResponseBodyAdvice(): EnvelopPatternResponseBodyAdvice {
        return EnvelopPatternResponseBodyAdvice()
    }

    @Bean
    @Order(2)
    fun cursorPaginationResponseBodyAdvice(): CursorPaginationAdvice {
        return CursorPaginationAdvice()
    }

    @Bean
    @Order(1)
    fun hateoasAdvice(applicationContext: ApplicationContext): HateoasAdvice {
        return HateoasAdvice(applicationContext)
    }
}