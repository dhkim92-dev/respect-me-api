package kr.respectme.common.advice

import jakarta.validation.ConstraintViolationException
import kr.respectme.common.error.BusinessException
import kr.respectme.common.error.GlobalErrorCode
import kr.respectme.common.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.support.MissingServletRequestPartException

@RestControllerAdvice
class GeneralExceptionHandlerAdvice{

    @ExceptionHandler(value = [BusinessException::class])
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(e.status).body(ErrorResponse.of(e.status, e.code, e.message))
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun methodArgumentTypeMismatchExceptionHandler(e: MethodArgumentTypeMismatchException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(e)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandler(e: MethodArgumentNotValidException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(GlobalErrorCode.INVALID_INPUT_VALUE, e.bindingResult)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationExceptionHandler(e: ConstraintViolationException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(GlobalErrorCode.INVALID_INPUT_VALUE, e.constraintViolations);
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun missingServletRequestParameterException(e: MissingServletRequestParameterException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(GlobalErrorCode.INVALID_INPUT_VALUE, "${e.parameterName} is missing.")
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MissingServletRequestPartException::class)
    fun missingServletRequestPartExceptionHandler(e: MissingServletRequestPartException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(GlobalErrorCode.INVALID_INPUT_VALUE, "${e.requestPartName} is missing.")
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MissingRequestCookieException::class)
    fun missingRequestCookieExceptionHandler(e: MissingRequestCookieException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(GlobalErrorCode.MISSING_COOKIE_VALUE, "${e.cookieName} is not in cookie.")
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun httpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException)
    : ResponseEntity<ErrorResponse> {
        val errors: MutableList<ErrorResponse.FieldError> = mutableListOf()
        errors.add(ErrorResponse.FieldError("http method", e.method, GlobalErrorCode.METHOD_NOT_ALLOWED.message))
        val response = ErrorResponse.of(GlobalErrorCode.METHOD_NOT_ALLOWED, errors)
        return ResponseEntity(response, GlobalErrorCode.METHOD_NOT_ALLOWED.status)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableExceptionHandler(e: HttpMessageNotReadableException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(GlobalErrorCode.HTTP_MESSAGE_NOT_READABLE)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

//    @ExceptionHandler(ExpiredTokenException::class)
//    fun expiredTokenExceptionHandler(e: ExpiredTokenException): ResponseEntity<ErrorResponse> {
//        logger.debug("expired token exception occured")
//        return ResponseEntity(ErrorResponse.of(ErrorCodes.EXPIRED_ACCESS_TOKEN_EXCEPTION), HttpStatus.UNAUTHORIZED)
//    }

    @ExceptionHandler
    fun exceptionHandler(e: Exception)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(GlobalErrorCode.INTERNAL_SERVER_ERROR,
            e.message?:GlobalErrorCode.INTERNAL_SERVER_ERROR.message)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}