package kr.respectme.message.application.dto

data class MessageBody (
    val type: MessageType = MessageType.GROUP_MESSAGE,
    val title: String,
    val body: String
) {

}