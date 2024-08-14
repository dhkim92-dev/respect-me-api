package kr.respectme.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans

@SpringBootApplication
@EnableFeignClients(basePackages = ["kr.respectme.common.security.jwt.adapter", "kr.respectme.auth"])
@ComponentScans(value=[ComponentScan("kr.respectme.common"), ComponentScan("kr.respectme.auth")])
class Application {
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}