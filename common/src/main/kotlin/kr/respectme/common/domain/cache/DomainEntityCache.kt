package kr.respectme.common.domain.cache

import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.enums.EntityStatus

interface DomainEntityCache {

    fun <T: BaseDomainEntity<*>> isSameWithCache(clazz: Class<T>, obj: T): EntityStatus

    fun <T: BaseDomainEntity<*>> get(clazz: Class<T>, obj: T): T?

    fun <T: BaseDomainEntity<*>> get(clazz: Class<T>, identifier: Any): T?

    fun <T: BaseDomainEntity<*>> get(clazz: Class<T>, key: String?): T?

    fun <T: BaseDomainEntity<*>> put(clazz: Class<T>, value: T): Boolean

    fun evict(key: String)

    fun <T: BaseDomainEntity<*>> evict(clazz: Class<T>, obj: T)

    fun <T: BaseDomainEntity<*>> evict(clazz: Class<T>, identifier: Any)

    fun evictAll()

    fun contains(key: String): Boolean

    fun <T: BaseDomainEntity<*>> contains(clazz: Class<T>, identifier: Any): Boolean

    fun <T: BaseDomainEntity<*>> contains(clazz: Class<T>, obj: T): Boolean
}