package kr.respectme.group.domain

import java.util.UUID

class GroupMember(
    val id: UUID,
    val nickname: String,
    val permissions: MutableList<GroupPermission>
){

}