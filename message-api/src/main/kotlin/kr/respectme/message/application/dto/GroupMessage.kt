package kr.respectme.message.application.dto

import java.util.*

class GroupMessage(
    val groupId: UUID = UUID.randomUUID(),
    val groupName: String = "",
    val notificationId: UUID = UUID.randomUUID(),
    val senderId: UUID = UUID.randomUUID(),
    val targets: List<String> = listOf(),
    val title: String = "",
    val body: String = ""
) {

}