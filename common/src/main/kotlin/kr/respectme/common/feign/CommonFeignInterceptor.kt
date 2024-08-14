package kr.respectme.common.feign

import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommonFeignInterceptor(
    @Value("\${respect-me.service-account.token}" )
    private val serviceAccountToken: String
) {

    @Bean
    fun internalFeignInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            template.header("Authorization", "Bearer ${serviceAccountToken}")
        }
    }
}