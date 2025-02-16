package kr.respectme.auth.configs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    @Value("\${server.domain}")
    private val domain: String
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun openAPI(): OpenAPI {

        return OpenAPI()
            .info(Info()
                .title("인증 API Documentation")
                .description("인증 API 문서입니다.")
            )
            .addServersItem(Server().url(domain))
    }
}