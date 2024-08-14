package kr.respectme.common.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationResponse(
    val status: Int,
    val message: String
) {

}