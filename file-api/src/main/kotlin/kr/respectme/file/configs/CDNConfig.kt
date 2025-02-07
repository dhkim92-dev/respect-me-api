package kr.respectme.file.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.util.UUID

@Configuration
class CDNConfig(
    @Value("\${respect-me.cdn.host}")
    val host: String,
    @Value("\${respect-me.cdn.storage-origin}")
    val storageOrigin: String
) {

}