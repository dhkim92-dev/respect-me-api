package kr.respectme.member_api.domain.model

enum class MemberRole(val roleName: String){

    ROLE_MEMBER("ROLE_MEMBER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUBSCRIPT_MEMBER("ROLE_SUBSCRIPT_MEMBER")
}