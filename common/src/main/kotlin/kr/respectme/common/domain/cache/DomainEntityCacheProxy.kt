package kr.respectme.common.domain.cache

interface DomainEntityCacheProxy: DomainEntityCache {

    fun getDomainEntityCache(): DomainEntityCache
}