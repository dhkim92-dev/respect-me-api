package kr.respectme.common.domain.cache

import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order

@Aspect
@Order(1)
class TransactionDomainEntityCacheAspect(
    private val domainEntityCacheFactory: DomainEntityCacheFactory
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("TransactionDomainEntityCacheAspect initialized.")
    }

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    fun beforeTransaction() {
        logger.info("transaction started hasProperty : ${DomainEntityCacheRepository.hasProperty(domainEntityCacheFactory)}")
        if(!DomainEntityCacheRepository.hasProperty(domainEntityCacheFactory)) {
            logger.info("repository does not have domain entity cache, creating new one")
            val entityCache = domainEntityCacheFactory.createCache()
            logger.info("created new domain entity cache ${entityCache}")
            DomainEntityCacheRepository.setProperty(domainEntityCacheFactory, entityCache)
        } else {
            logger.info("repository already has domain entity cache")
        }
    }

    @After("@annotation(org.springframework.transaction.annotation.Transactional)")
    fun afterTransaction() {
        if(DomainEntityCacheRepository.hasProperty(domainEntityCacheFactory)) {
            logger.debug("Transaction ended")
            val domainEntityCache = (DomainEntityCacheRepository.getProperty(domainEntityCacheFactory)) as DomainEntityCache
            domainEntityCache.evictAll()
            DomainEntityCacheRepository.removeProperty(domainEntityCacheFactory)
            logger.debug("Domain entity cache is cleared")
        }
        DomainEntityCacheRepository.clear()
    }
}