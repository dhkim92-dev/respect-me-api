package kr.respectme.common.domain.cache

import com.fasterxml.jackson.databind.ObjectMapper

class InMemoryDomainEntityCacheFactory(
    private val objectMapper: ObjectMapper
): DomainEntityCacheFactory {

    override fun createCache(): DomainEntityCache {
        return InMemoryDomainEntityCache(objectMapper)
    }
}