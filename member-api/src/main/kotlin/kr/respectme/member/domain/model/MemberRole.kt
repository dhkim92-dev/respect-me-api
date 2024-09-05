package kr.respectme.member.domain.model

enum class MemberRole(val roleName: String, val dbValue: Int){

    ROLE_SERVICE("ROLE_SERVICE", 1),
    ROLE_ADMIN("ROLE_ADMIN", 2),
    ROLE_MEMBER("ROLE_MEMBER", 3),
}