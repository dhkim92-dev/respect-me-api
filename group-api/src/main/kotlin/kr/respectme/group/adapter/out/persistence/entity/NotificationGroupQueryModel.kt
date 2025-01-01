package kr.respectme.group.application.dto.group

import kr.respectme.group.domain.GroupMemberRole
import kr.respectme.group.port.`in`.interfaces.dto.GroupMemberVo
import java.time.Instant
import java.util.*

data class NotificationGroupQueryModel(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val description: String = "",
    val ownerInfo: GroupMemberVo = GroupMemberVo(),
    val memberCount: Int = 0,
    val createdAt: Instant = Instant.now(),
    val groupThumbnail: String? = null,
    val myRole: GroupMemberRole = GroupMemberRole.GUEST
) {

}