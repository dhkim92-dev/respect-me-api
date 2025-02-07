package kr.respectme.file.common.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Constraint(validatedBy = [ImageBytesValidator::class])
annotation class ImageBytes(
    val message: String = "이미지 파일이 아닙니다",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
) {

}