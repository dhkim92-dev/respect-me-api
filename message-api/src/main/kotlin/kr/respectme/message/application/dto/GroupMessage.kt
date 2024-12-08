package kr.respectme.message.application.dto

import java.util.*

class GroupMessage(
    val groupId: UUID = UUID.randomUUID(),
    val groupImageUrl : String? = null,
    val senderId: UUID = UUID.randomUUID(),
    val notificationId: UUID = UUID.randomUUID(),
    val title: String = "",
    val contents: String = "",
    val groupName: String = ""
) {

}