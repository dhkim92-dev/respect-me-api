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
    TOO_LONG_NICKNAME("Nickname is too long.", "AUTH-007", HttpStatus.BAD_REQUEST),
    NOT_SUPPORTED_OIDC_PROVIDER("Not supported OIDC provider.", "AUTH-008", HttpStatus.BAD_REQUEST),
    NOT_REGISTERED_OIDC_USER("Not registered OIDC user.", "AUTH-009", HttpStatus.UNAUTHORIZED),
    OIDC_FETCH_JWKS_FAILED("Failed to fetch JWKS.", "AUTH-010", HttpStatus.UNAUTHORIZED),
    OIDC_ID_TOKEN_VERIFICATION_FAILED("Failed to verify ID token.", "AUTH-011", HttpStatus.UNAUTHORIZED),
    FAILED_TO_VERIFY_SERVICE("Failed to verify service.", "AUTH-012", HttpStatus.UNAUTHORIZED),
    REQUIRE_NICKNAME_TO_JOIN_SERVICE_BY_OIDC("Require nickname to join service by OIDC.", "AUTH-013", HttpStatus.BAD_REQUEST),
    ONLY_SERVICE_CAN_ACCESS("Only service can access.", "AUTH-014", HttpStatus.FORBIDDEN),
    AUTHENTICATION_INFO_EXISTS_BUT_NO_MEMBER_INFO("Authentication info exists but no member info.", "AUTH-015", HttpStatus.UNAUTHORIZED),
}