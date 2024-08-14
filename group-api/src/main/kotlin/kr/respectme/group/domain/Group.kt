package kr.respectme.group.domain

import java.time.LocalDateTime
import java.util.UUID

class Group(
    val groupId: UUID,
    var groupName: String,
    var members: MutableList<GroupMember>,
    var createdAt: LocalDateTime,
    var lastMessageAt: LocalDateTime,
) {

}