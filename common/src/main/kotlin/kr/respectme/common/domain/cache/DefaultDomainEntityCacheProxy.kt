package kr.respectme.common.domain.cache

import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.enums.EntityStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DefaultDomainEntityCacheProxy(
    private val domainEntityCacheFactory: DomainEntityCacheFactory,
): DomainEntityCacheProxy {

    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        logger.info("DefaultDomainEntityCacheProxy initialized.")
    }

    override fun <T : BaseDomainEntity<*>> isSameWithCache(clazz: Class<T>, obj: T): EntityStatus {
        val cache = DomainEntityCacheRepository.getProperty(domainEntityCacheFactory)
        return getDomainEntityCache().isSameWithCache(clazz, obj)
    }

    override fun <T : BaseDomainEntity<*>> get(clazz: Class<T>, obj: T): T? {
        return getDomainEntityCache().get(clazz, obj)
    }

    override fun <T : BaseDomainEntity<*>> get(clazz: Class<T>, identifier: Any): T? {
        return getDomainEntityCache().get(clazz, identifier)
    }

    override fun <T : BaseDomainEntity<*>> get(clazz: Class<T>, key: String?): T? {
        return getDomainEntityCache().get(clazz, key)
    }

    override fun <T : BaseDomainEntity<*>> put(clazz: Class<T>, value: T): Boolean {
        return getDomainEntityCache().put(clazz, value)
    }

    override fun evict(key: String) {
        val cache = DomainEntityCacheRepository.getProperty(domainEntityCacheFactory)
        getDomainEntityCache().evict(key)
    }

    override fun <T : BaseDomainEntity<*>> evict(clazz: Class<T>, obj: T) {
        getDomainEntityCache().evict(clazz, obj)
    }

    override fun <T : BaseDomainEntity<*>> evict(clazz: Class<T>, identifier: Any) {
        getDomainEntityCache().evict(clazz, identifier)
    }

    override fun evictAll() {
        getDomainEntityCache().evictAll()
    }

    override fun contains(key: String): Boolean {
        return getDomainEntityCache().contains(key)
    }

    override fun <T : BaseDomainEntity<*>> contains(clazz: Class<T>, identifier: Any): Boolean {
        return getDomainEntityCache().contains(clazz, identifier)
    }

    override fun <T : BaseDomainEntity<*>> contains(clazz: Class<T>, obj: T): Boolean {
        return getDomainEntityCache().contains(clazz, obj)
    }

    override fun getDomainEntityCache(): DomainEntityCache {
        return DomainEntityCacheRepository.getProperty(domainEntityCacheFactory) as DomainEntityCache
    }
}