package kr.respectme.common.annotation

import org.springframework.http.HttpStatus
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationResponse(
    val status: HttpStatus,
    val message: String
) {

}