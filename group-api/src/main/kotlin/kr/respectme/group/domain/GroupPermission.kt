package kr.respectme.group.domain

enum class GroupPermission(value: String) {
    MESSAGE_READ("message:read"),
    MESSAGE_WRITE("message:write"),
    MESSAGE_DELETE("message:delete"),
    MEMBER_INVITE("member:invite"),
    MEMBER_KICK("member:kick"),
    MEMBER_READ("member:read"),
    MEMBER_WRITE("member:write"),
}