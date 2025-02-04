package kr.respectme.common.domain.cache

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.domain.annotations.DomainRelation
import kr.respectme.common.domain.annotations.IgnoreField
import kr.respectme.common.domain.enums.EntityStatus
import org.slf4j.LoggerFactory
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaMethod

class InMemoryDomainEntityCache(private val objectMapper: ObjectMapper): DomainEntityCache {

    private val snapshot = mutableMapOf<String, BaseDomainEntity<*>>()

    private val logger = LoggerFactory.getLogger(InMemoryDomainEntityCache::class.java)

    /**
     * 오브젝트가 캐시에 없으면 -1, 신규 엔티티임을 의미
     * 오브젝트가 캐시에 있고, 캐시와 비교하여 프로퍼티가 변경되었으면 1, 수정된 엔티티임을 의미
     * 오브젝트가 캐시에 있고, 캐시와 비교하여 프로퍼티가 변경되지 않았으면 0, 수정되지 않은 엔티티임을 의미
      */
    override fun <T : BaseDomainEntity<*>> isSameWithCache(clazz: Class<T>, obj: T): EntityStatus {
        val cached = snapshot[getKey(clazz, obj.id)]
        return if(cached == null) {
            EntityStatus.CREATED
        } else {
            if(compareProperties(obj, cached)) {
                EntityStatus.UPDATED
            } else {
                EntityStatus.ACTIVE
            }
        }
    }

    override fun <T: BaseDomainEntity<*>> get(clazz: Class<T>, obj: T): T? {
        logger.debug("${clazz.name} get called")
        obj.id?.let {
            val key = getKey(clazz, it)
            return this.get(clazz, key)
        }
        logger.debug("${clazz.name} got ${obj}")
        return null
    }

    override fun <T: BaseDomainEntity<*>> get(clazz: Class<T>, identifier: Any): T? {
        val key = getKey(clazz, identifier)
        return this.get(clazz, key)
    }

    override fun <T: BaseDomainEntity<*>> get(clazz: Class<T>, key: String?): T? {
        val cached = snapshot[key]?.let { clazz.cast(it) }
        return cached
    }

    override fun <T : BaseDomainEntity<*>> put(clazz: Class<T>, value: T): Boolean {
        if(clazz.isAnnotationPresent(DomainEntity::class.java).not()) {
//            logger.error("Class is not annotated with DomainEntity")
            return false
        }

        val key = getKey(clazz, value.id)
        key?.let{
            snapshot[key] = copyObject(value)
            logger.debug("${clazz.name} instance ${key} will be cached.")
            logger.debug("cache info ${objectMapper.writeValueAsString(snapshot[key])}")
            return true
        }
        return false
    }

    override fun evict(key: String) {
//        logger.debug("key will be evicted.")
        snapshot.remove(key)
    }

    override fun <T: BaseDomainEntity<*>> evict(clazz: Class<T>, obj: T) {
        obj.id?.let{this.evict(clazz, obj.id)} ?: return
    }

    override fun <T : BaseDomainEntity<*>> evict(clazz: Class<T>, identifier: Any) {
        val key = getKey(clazz, identifier)
        key?.let{snapshot.remove(key)}
    }

    override fun evictAll() {
        snapshot.clear()
    }

    override fun contains(key: String): Boolean {
        return snapshot.containsKey(key)
    }

    override fun <T : BaseDomainEntity<*>> contains(clazz: Class<T>, identifier: Any): Boolean {
        val key = getKey(clazz, identifier)
        return snapshot.containsKey(key)
    }

    override fun <T : BaseDomainEntity<*>> contains(clazz: Class<T>, obj: T): Boolean {
        obj.id?.let {
            return this.contains(clazz, it)
        }
        return false
    }

    private fun <T: BaseDomainEntity<*>> getKey(clazz: Class<T>, identifier: Any?): String? {
        if(identifier == null) {
            logger.error("Identifier is null")
            return null
        }
        return "${clazz.name}::${identifier}"
    }

    /**
     * 두 오브젝트의 프로퍼티를 비교하여 변경된 프로퍼티가 있는지 확인
     * @param obj 비교할 오브젝트
     * @param cached 캐시된 오브젝트
     * @return 변경된 프로퍼티가 있으면 true, 없으면 false
     */
    private fun <T: BaseDomainEntity<*>> compareProperties(obj: T, cached: T): Boolean {
        logger.debug("compareProperties called")
        if(obj::class != cached::class) {
            logger.error("Class is not same")
            return false
        }

        val targetProperties = obj::class.memberProperties
            /* DomainRelation, IgnoreField 어노테이션 제외 */
            .filter {
                val hasDomainRelationAnnotation = it.javaField?.isAnnotationPresent(DomainRelation::class.java) == true ||
                        it.getter.javaMethod?.isAnnotationPresent(DomainRelation::class.java) == true

                val hasIgnoreFieldAnnotation = it.javaField?.isAnnotationPresent(IgnoreField::class.java) == true ||
                        it.getter.javaMethod?.isAnnotationPresent(IgnoreField::class.java) == true

                !hasDomainRelationAnnotation && !hasIgnoreFieldAnnotation
            }

        logger.debug("targetProperties size : ${targetProperties.size}")

        val result =  targetProperties.any { property ->
            property.isAccessible = true
            val objValue = property.getter.call(obj)
            val cachedValue = property.getter.call(cached)
            val propertyCompareResult = objValue != cachedValue
            logger.debug("property name : ${property.name}, objValue : ${objValue}, cachedValue : ${cachedValue} same? : ${objValue == cachedValue}")
            propertyCompareResult
        }

        logger.debug("compare result : $result")
        return result
    }

    private fun <T> copyObject(obj: T): T {
        logger.debug("copyObject called")
        return objectMapper.writeValueAsString(obj).let { objectMapper.readValue(it, obj!!::class.java) }
    }
}