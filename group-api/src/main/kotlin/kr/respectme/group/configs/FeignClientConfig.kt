package kr.respectme.group.configs

import feign.RequestInterceptor
import jakarta.servlet.http.HttpServletResponse
import kr.respectme.common.annotation.ServiceAccount
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClientFactory
import org.springframework.cloud.openfeign.FeignClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Configuration
class FeignClientConfig(private val serviceConfig: ServiceConfig) {

    @Bean
    fun feignClientFactory(): FeignClientFactory {
        return FeignClientFactory()
    }

    @Bean
    fun feignClientProperties(): FeignClientProperties {
        return FeignClientProperties()
    }

    @Bean
    fun headerInterceptor(): RequestInterceptor {

        return RequestInterceptor { template ->
            if(template.url().startsWith("/internal/api/**")) {
                template.header("Authorization", "Bearer ${serviceConfig.accessToken}")
            } else {
                (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes)
                .request
                .getHeader("Authorization")
                ?.let { bearer ->
                    template.header("Authorization", bearer)
                }
            }
        }
    }
}