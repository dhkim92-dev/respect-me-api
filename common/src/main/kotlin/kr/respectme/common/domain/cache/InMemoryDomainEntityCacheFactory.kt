package kr.respectme.common.domain.cache

class InMemoryDomainEntityCacheFactory: DomainEntityCacheFactory {

    override fun createCache(): DomainEntityCache {
        return InMemoryDomainEntityCache()
    }
}