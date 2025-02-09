package kr.respectme.report

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
@EnableAutoConfiguration(exclude = [UserDetailsServiceAutoConfiguration::class])
class Application {
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}