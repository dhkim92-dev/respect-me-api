package kr.respectme.common.support

import kr.respectme.common.domain.DomainEntityCacheAutoConfiguration
import kr.respectme.common.domain.cache.InMemoryDomainEntityCacheFactory
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DomainEntityConfig(
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun domainEntityCacheConfiguration(): DomainEntityCacheAutoConfiguration {
        logger.info("DomainEntityCacheAutoConfiguration initialized.")
        return DomainEntityCacheAutoConfiguration()
    }
}