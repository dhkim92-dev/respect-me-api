package kr.respectme.common.advice.hateoas

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Hateoas(val converter: KClass<out HateoasConverter>)
