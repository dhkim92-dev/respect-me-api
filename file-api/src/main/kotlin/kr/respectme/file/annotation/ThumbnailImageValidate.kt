package kr.respectme.file.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [MultipartFileImageValidateProcessor::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class MultipartFileImageValidate(
    val message: String = "Not supported content type(image/jpg, image/jpeg, image/png) or size is not 128x128",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
}
