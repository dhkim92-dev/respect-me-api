package kr.respectme.auth.configs

import feign.RequestInterceptor
import feign.codec.ErrorDecoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig(@Value("\${respect-me.service-account.token}") val serviceToken: String){

    @Bean
    fun feignInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            template.header("Authorization", "Bearer ${serviceToken}")
        }
    }
}