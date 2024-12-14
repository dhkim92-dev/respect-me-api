package kr.respectme.common.domain.cache

interface DomainEntityCacheFactory {

    fun createCache(): DomainEntityCache
}