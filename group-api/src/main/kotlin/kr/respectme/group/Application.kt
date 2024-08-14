package kr.respectme.group

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans

@SpringBootApplication
@ComponentScan(basePackages = ["kr.respectme.common", "kr.respectme.group"])
@EnableFeignClients(basePackages = ["kr.respectme.common.security.jwt.adapter", "kr.respectme.group"])
class Application {}
fun main(args: Array<String>) {
    runApplication<Application>(*args)
}