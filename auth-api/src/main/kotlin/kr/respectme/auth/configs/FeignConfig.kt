package kr.respectme.auth.configs

import feign.RequestInterceptor
import feign.codec.ErrorDecoder
import io.micrometer.tracing.Tracer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig(
    @Value("\${respect-me.service-account.token}") val serviceToken: String,
    private val tracer: Tracer,
){

    @Bean
    fun feignInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            val span = tracer.currentSpan()
            span?.let {
                template.header("X-B3-TraceId", it.context().traceId())
                template.header("X-B3-SpanId", it.context().spanId())
            }
            template.header("Authorization", "Bearer ${serviceToken}")
        }
    }
}