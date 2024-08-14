package kr.respectme.member.common.code

import kr.respectme.common.error.ErrorCode
import org.springframework.http.HttpStatus

enum class MemberServiceErrorCode(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
): ErrorCode{

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-ERR-0001", "Query result is null."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "MEMBER-ERR-0002", "Email/Password mismatch."),
    RESOURCE_OWNERSHIP_VIOLATION(HttpStatus.FORBIDDEN, "MEMBER-ERR-003", "No permission to access resource."),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "MEMBER-ERR-004", "Already used email."),
    ;
}