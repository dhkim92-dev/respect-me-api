package kr.respectme.member.common.code

import kr.respectme.common.error.ErrorCode
import org.springframework.http.HttpStatus

enum class MemberServiceErrorCode(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
): ErrorCode{

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-ERR-0001", "Member not exists."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "MEMBER-ERR-0002", "Email/Password mismatch."),
    RESOURCE_OWNERSHIP_VIOLATION(HttpStatus.FORBIDDEN, "MEMBER-ERR-003", "No permission to access resource."),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "MEMBER-ERR-004", "Already used email."),
    DEVICE_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-ERR-005", "Device token not found."),
    FAILED_TO_DELETE_DEVICE_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER-ERR-006", "Device token delete failed."),
    EXCEEDED_DEVICE_TOKEN_LIMIT(HttpStatus.BAD_REQUEST, "MEMBER-ERR-007", "Exceeded device token limit."),
    INVALID_DEVICE_TOKEN(HttpStatus.BAD_REQUEST, "MEMBER-ERR-008", "Invalid device token."),
    INVALID_DEVICE_TOKEN_TYPE(HttpStatus.BAD_REQUEST, "MEMBER-ERR-009", "Invalid device token type."),
    ;
}