package kr.respectme.common.annotation

import kr.respectme.common.response.ResponseCode
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationResponse(val responseCode: KClass<out ResponseCode>) {

}