package kr.respectme.message.configs

import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig(
    @Value("\${respect-me.service-account.token}")
    val accessToken: String
) {

    @Bean
    fun feignInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            println("request body : ${template.body()?.let{String(it)}}")
            template.header("Authorization", "Bearer ${accessToken}")
        }
    }
}