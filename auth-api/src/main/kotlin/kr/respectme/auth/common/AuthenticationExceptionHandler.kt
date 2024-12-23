package kr.respectme.auth.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.FirebaseAuthException
import kr.respectme.common.advice.GeneralExceptionHandlerAdvice
import kr.respectme.common.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class AuthenticationExceptionHandler(
    private val objectMapper: ObjectMapper
): GeneralExceptionHandlerAdvice(objectMapper = objectMapper) {

    @ExceptionHandler(FirebaseAuthException::class)
    fun handleFirebaseAuthException(e: FirebaseAuthException): ErrorResponse{
        return ErrorResponse(
            status = HttpStatus.UNAUTHORIZED,
            message = e.message ?: "Firebase Auth Exception"
        )
    }
}