package kr.respectme.common.advice.hateoas

import org.slf4j.LoggerFactory
import kotlin.reflect.full.hasAnnotation

abstract class AbstractHateoasConverter<T: HateoasResponse>: HateoasConverter {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun convert(element: HateoasResponse): HateoasResponse {
        if ( !isSupport(element) ) {
//            logger.error("element type : ${element::class.jvmName} is not supported Hateoas")
            throw IllegalArgumentException("지원하지 않는 타입입니다.")
        }

        translate(element as T)

        return element
    }

    protected open fun isSupport(element: Any): Boolean {
        return element::class.hasAnnotation<Hateoas>()
                && HateoasResponse::class.java.isAssignableFrom(element::class.java)
    }

    protected abstract fun translate(element: T)
}