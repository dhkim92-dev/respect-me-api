package kr.respectme.group.common.errors

import kr.respectme.common.error.ErrorCode
import org.springframework.http.HttpStatus

enum class GroupServiceErrorCode(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
): ErrorCode {

    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "GRP-001", "Group not found"),
    GROUP_ALREADY_EXISTS(HttpStatus.CONFLICT, "GRP-G002", "Group already exists"),
    GROUP_OWNER_CANT_LEAVE(HttpStatus.BAD_REQUEST, "GRP-003", "Group notification type is empty"),
    GROUP_PASSWORD_MISMATCH(HttpStatus.FORBIDDEN, "GRP-004", "Group password mismatch"),

    GROUP_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "GRPM-001", "Group member not found"),
    GROUP_MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "GRPM-002", "Group member already exists"),
    GROUP_MEMBER_NOT_OWNER(HttpStatus.FORBIDDEN, "GRPM-003", "Only Group owner can execute this request"),
    GROUP_MEMBER_NOT_MEMBER(HttpStatus.FORBIDDEN, "GRPM-004", "You are not a member of this group"),
    GROUP_MEMBER_NOT_ADMIN(HttpStatus.FORBIDDEN, "GRPM-005", "Group member is not admin"),
    GROUP_MEMBER_NOT_ENOUGH_PERMISSION(HttpStatus.FORBIDDEN, "GRPM-006", "Group member does not have enough permission"),

    GROUP_NOTIFICATION_ALREADY_PUBLISHED(HttpStatus.BAD_REQUEST, "GRPN-001", "Group notification already published"),
    GROUP_NOTIFICATION_CONTENTS_EMPTY(HttpStatus.BAD_REQUEST, "GRPN-002", "Group notification contents is empty"),
    GROUP_NOTIFICATION_CONTENTS_TOO_LONG(HttpStatus.BAD_REQUEST, "GRPN-003", "Group notification contents length must smaller than 1000 characters"),
    GROUP_NOTIFICATION_RESERVED_AT_EMPTY(HttpStatus.BAD_REQUEST, "GRPN-004", "Group notification type is schedules, but scheduledAt is empty."),
    GROUP_NOTIFICATION_GROUP_ID_MISMATCH(HttpStatus.BAD_REQUEST, "GRPN-005", "Group notification group id mismatch"),
    GROUP_NOTIFICATION_CANNOT_UPDATE_CONTENTS(HttpStatus.BAD_REQUEST, "GRPN-006", "Group notification is immediately type, so cannot update contents"),
    GROUP_NOTIFICATION_SENDER_MISMATCH(HttpStatus.FORBIDDEN, "GRPN-007", "Group notification sender mismatch"),
    GROUP_NOTIFICATION_INVALID_TYPE(HttpStatus.BAD_REQUEST, "GRPN-008", "Group notification type is invalid"),
    GROUP_NOTIFICATION_RESERVED_AT_INVALID(HttpStatus.BAD_REQUEST, "GRPN-009", "ReservedAt must be future time"),
}