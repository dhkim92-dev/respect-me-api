package kr.respectme.common.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CursorParam(val key: String, val inherit: Boolean = false) {

}
