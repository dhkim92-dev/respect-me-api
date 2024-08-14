package kr.respectme.common.error

import org.springframework.http.HttpStatus

enum class GlobalErrorCode(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
): ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Common-E0001", "Invalid input value."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Common-E0002", "Internal server error."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Common-E0003", "Method not allowed."),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "Common-E0004", "Missing request parameter."),
    MISSING_REQUEST_PART(HttpStatus.BAD_REQUEST, "Common-E0005", "Missing request part."),
    MISSING_COOKIE_VALUE(HttpStatus.BAD_REQUEST, "Common-E0006", "Missing cookie value."),
    HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "Common-E0007", "Http message not readable."),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "Common-E0008", "Unauthorized."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN, "Common-E0009", "Access denied."),
    UNSUPPORTED_PRINCIPAL_EXCEPTION(HttpStatus.FORBIDDEN, "Common-E0010", "Unsupported principal."),
    REQUIRE_SERVICE_ACCOUNT_EXCEPTION(HttpStatus.FORBIDDEN, "Common-E0011", "Service Account required."),
    JWT_PAYLOAD_VERSION_MISMATCH(HttpStatus.UNAUTHORIZED, "Common-E0012", "JWT decoded but fields are not compatible with this version."),
    ;
}