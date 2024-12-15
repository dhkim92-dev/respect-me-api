package kr.respectme.common.domain

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.domain.annotations.DomainEntityAnnotationProcessor
import kr.respectme.common.domain.cache.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.AutoConfigurationPackage
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
@AutoConfigurationPackage
class DomainEntityCacheAutoConfiguration() {

    @Autowired private lateinit var environment: Environment
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnMissingBean(DomainEntityCacheFactory::class)
    fun cacheFactory(objectMapper: ObjectMapper): DomainEntityCacheFactory {
        logger.info("DomainEntityCacheFactory initialized.")
        return InMemoryDomainEntityCacheFactory(objectMapper)
    }

    @Bean
    fun domainEntityAnnotationProcessor(): DomainEntityAnnotationProcessor {
        return DomainEntityAnnotationProcessor()
    }

    @Bean
    fun transactionalDomainEntityCacheAspect(domainEntityCacheFactory: DomainEntityCacheFactory): TransactionDomainEntityCacheAspect {
        logger.info("TransactionalDomainEntityCacheAspect initialized.")
        return TransactionDomainEntityCacheAspect(domainEntityCacheFactory)
    }

    @Bean
    @ConditionalOnMissingBean(DomainEntityCacheProxy::class)
    fun domainEntityCacheProxy(domainEntityCacheFactory: DomainEntityCacheFactory): DomainEntityCacheProxy {
        return DefaultDomainEntityCacheProxy(domainEntityCacheFactory)
    }

    private fun getBasePackageName(): String {
        val mainClassName = environment.getProperty("spring.main.sources")
            ?: throw IllegalStateException("Main class not found")
        return mainClassName.substringBeforeLast(".")
    }
}