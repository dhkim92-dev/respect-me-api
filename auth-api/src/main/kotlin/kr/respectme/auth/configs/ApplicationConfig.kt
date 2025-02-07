package kr.respectme.auth.configs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.advice.CursorPaginationAdvice
import kr.respectme.common.advice.EnvelopPatternResponseBodyAdvice
import kr.respectme.common.advice.GeneralExceptionHandlerAdvice
import kr.respectme.common.domain.DomainEntityCacheAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
class ApplicationConfig(private val objectMapper: ObjectMapper) {

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(HttpClient.create().followRedirect(true)))
            .build()
    }

//    @Bean
//    fun exceptionHandler() : GeneralExceptionHandlerAdvice {
//        return GeneralExceptionHandlerAdvice(objectMapper)
//    }
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    fun envelopResponseAdvice(): EnvelopPatternResponseBodyAdvice {
//        return EnvelopPatternResponseBodyAdvice()
//    }
//
//    @Bean
//    @Order(Ordered.LOWEST_PRECEDENCE)
//    fun cursorPaginationAdvice(): CursorPaginationAdvice {
//        return CursorPaginationAdvice()
//    }
}