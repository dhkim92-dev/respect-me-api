package kr.respectme.group.adapter.out.persistence

import java.util.UUID

data class GroupInfoVo(
    val id: UUID,
    val name: String,
    val imageId: Long? = null
) {
}