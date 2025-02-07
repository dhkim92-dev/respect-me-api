package kr.respectme.file.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceAccountConfig(
    @Value("\${respect-me.service-account.token}")
    val accessToken: String
) {

}