package kr.respectme.common.support

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestConfig {

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("TestConfig initialized.")
    }
}