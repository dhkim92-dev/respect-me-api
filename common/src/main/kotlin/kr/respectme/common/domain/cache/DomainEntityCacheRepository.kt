package kr.respectme.common.domain.cache

import org.slf4j.LoggerFactory
import java.lang.ThreadLocal

abstract class DomainEntityCacheRepository {

    companion object {
        private val logger = LoggerFactory.getLogger(javaClass)

        private val properties: ThreadLocal<MutableMap<Any, Any>> = ThreadLocal.withInitial {
            mutableMapOf()
        }

        fun setProperty(key: Any, value: Any) {
            properties.get()[key] = value
        }

        fun getProperty(key: Any): Any? {
            return properties.get()[key]
        }

        fun hasProperty(key: Any): Boolean {
            return properties.get()?.containsKey(key)
                ?: false
        }

        fun removeProperty(key: Any) {
            properties.get().remove(key)
        }

        fun clear() {
            properties.remove()
        }
    }
}