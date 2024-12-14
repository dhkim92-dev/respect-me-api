package kr.respectme.common.domain.cache

import com.google.gson.Gson
import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.domain.annotations.DomainRelation
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.gson.GsonProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaMethod

class InMemoryDomainEntityCache: DomainEntityCache {

    private val snapshot = mutableMapOf<String, BaseDomainEntity<*>>()

    private val logger = LoggerFactory.getLogger(InMemoryDomainEntityCache::class.java)


    /**
     * 오브젝트가 캐시에 없으면 -1, 신규 엔티티임을 의미
     * 오브젝트가 캐시에 있고, 캐시와 비교하여 프로퍼티가 변경되었으면 1, 수정된 엔티티임을 의미
     * 오브젝트가 캐시에 있고, 캐시와 비교하여 프로퍼티가 변경되지 않았으면 0, 수정되지 않은 엔티티임을 의미
      */
    override fun <T : BaseDomainEntity<*>> isModified(clazz: Class<T>, obj: T): Int {
        obj.id?.let {
            val key = getKey(clazz, it)
            get(clazz, key)?.let {
                return compareProperties(obj, it).let { if(it) 1 else 0 }
            }
        }
        return -1
    }

    override fun <T: BaseDomainEntity<*>> get(clazz: Class<T>, obj: T): T? {
        obj.id?.let {
            val key = getKey(clazz, it)
            return this.get(clazz, key)
        }
        return null
    }

    override fun <T: BaseDomainEntity<*>> get(clazz: Class<T>, identifier: Any): T? {
        val key = getKey(clazz, identifier)
        return this.get(clazz, key)
    }

    override fun <T: BaseDomainEntity<*>> get(clazz: Class<T>, key: String?): T? {
        return snapshot[key]?.let { clazz.cast(it) }
    }

    override fun <T : BaseDomainEntity<*>> put(clazz: Class<T>, value: T): Boolean {
        if(clazz.isAnnotationPresent(DomainEntity::class.java).not()) {
            logger.error("Class is not annotated with DomainEntity")
            return false
        }

        val key = getKey(clazz, value.id)
        key?.let{
            snapshot[key] = copyObject(value)
            return true
        }
        return false
    }

    override fun evict(key: String) {
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
        if(obj::class != cached::class) {
            logger.error("Class is not same")
            return false
        }

        val targetProperties = obj::class.memberProperties.filter{
            val fieldHasAnnotation = it.javaField?.isAnnotationPresent(DomainRelation::class.java) == true
            val getterHasAnnotation = it.getter.javaMethod?.isAnnotationPresent(DomainRelation::class.java) == true
//            logger.info("Property: ${it.name}, Field Annotation: $fieldHasAnnotation, Getter Annotation: $getterHasAnnotation")
            !fieldHasAnnotation && !getterHasAnnotation
        }

        return targetProperties.any { property ->
            val objValue = property.getter.call(obj)
            val cachedValue = property.getter.call(cached)
//            logger.info("cached value : ${cachedValue}, current value : ${objValue}")
            objValue != cachedValue
        }
    }

    private fun <T> copyObject(obj: T): T {
        return Gson().fromJson(Gson().toJson(obj), obj!!::class.java)
    }
}