package kr.respectme.common.response

import jakarta.validation.ConstraintViolation
import kr.respectme.common.error.ErrorCode
import kr.respectme.common.error.GlobalErrorCode.INVALID_INPUT_VALUE
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

class ErrorResponse(
    val traceId: String? = MDC.get("traceId"),
    override val status: HttpStatus = HttpStatus.OK,
    override val code: String = "",
    override val message: String? = null,
    var errors: List<FieldError> = listOf()
) : ResponseCode {

    companion object {

        fun of(code: ErrorCode, bindingResult: BindingResult)
        = ErrorResponse(code = code.code, message = code.message, status = code.status, errors =FieldError.of(bindingResult))

        fun of(code: ErrorCode)
        = ErrorResponse(code = code.code, message = code.message, status = code.status)

        fun of(status: HttpStatus, message: String, code: String)
        = ErrorResponse(code = code, message = message, status = status)

        fun of(code: ErrorCode, violations: Set<ConstraintViolation<*>>)
        = ErrorResponse(code = code.code, message = code.message, status = code.status, errors =FieldError.of(violations))

        fun of(code: ErrorCode, errors: List<FieldError>)
        = ErrorResponse(code = code.code, message = code.message, status = code.status, errors =errors)

        fun of(code: ErrorCode, message: String)
        = ErrorResponse(code = code.code, message = message, status = code.status)

        fun of(e: MethodArgumentTypeMismatchException): ErrorResponse {
            val value = e.value?.toString() ?: ""
            val errors = FieldError.of(e.name, value, e.errorCode);
            return ErrorResponse(status=INVALID_INPUT_VALUE.status, code=INVALID_INPUT_VALUE.code, message = e.value?.toString(), errors=errors);
        }
    }


    class FieldError(
        val field: String,
        val value: String,
        val reason: String
    ) {

        companion object {
            fun of(field: String, value: String, reason: String): List<FieldError> {
                return listOf(FieldError(field, value, reason))
            }

            fun of(bindingResult: BindingResult): List<FieldError> {
                val fieldErrors: List<org.springframework.validation.FieldError> = bindingResult.fieldErrors

                return fieldErrors.stream().map {
                    FieldError(
                        it.field,
                        it.rejectedValue?.toString() ?: "",
                        it.defaultMessage ?: ""
                    )
                }.toList()
            }

            fun of(constraintViolations: Set<ConstraintViolation<*>>): List<FieldError> {
                return constraintViolations.toList()
                    .map { error ->
                        val invalidValue = error.invalidValue?.toString() ?: ""
                        val index = error.propertyPath.toString().indexOf(".")
//                        val propertyPath = error.propertyPath.toString().substring(index + 1)
                        val propertyPath = error.propertyPath.toString().split(".").last()
                        FieldError(propertyPath, invalidValue, error.message ?: "Validation error")
                    }
            }
        }
    }
}