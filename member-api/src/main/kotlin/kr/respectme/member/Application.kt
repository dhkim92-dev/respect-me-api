package kr.respectme.member

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans

@SpringBootApplication
@ComponentScan(basePackages = ["kr.respectme.common", "kr.respectme.member"])
@EnableFeignClients(basePackages = ["kr.respectme.common.security.jwt.adapter", "kr.respectme.member"])
class Application {}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}