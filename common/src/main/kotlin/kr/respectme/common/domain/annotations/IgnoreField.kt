package kr.respectme.common.domain.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(*[AnnotationTarget.FIELD, AnnotationTarget.PROPERTY])
annotation class IgnoreField()
