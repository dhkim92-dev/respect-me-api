package kr.respectme.file.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class MsaConfig(
    @Value("\${server.domain}")
    private val gatewayUrl: String
) {

    fun getGatewayUrl(): String {
        return gatewayUrl
    }
}