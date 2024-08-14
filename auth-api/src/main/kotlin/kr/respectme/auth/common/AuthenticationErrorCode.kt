package kr.respectme.auth.common

import kr.respectme.common.error.ErrorCode
import org.springframework.http.HttpStatus

enum class AuthenticationErrorCode(
    override val message: String,
    override val code: String,
    override val status: HttpStatus
): ErrorCode {

    FAILED_TO_SIGN_IN("Failed to sign in, Check your email or password.", "AUTH-001", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("Invalid refresh token.", "AUTH-003", HttpStatus.BAD_REQUEST),
    INVALID_ACCESS_TOKEN("Invalid access token.", "AUTH-004", HttpStatus.BAD_REQUEST),
    EXPIRED_ACCESS_TOKEN("Expired access token.", "AUTH-005", HttpStatus.UNAUTHORIZED),
    EXPIRED_REFRESH_TOKEN("Expired refresh token.", "AUTH-006", HttpStatus.UNAUTHORIZED),
}