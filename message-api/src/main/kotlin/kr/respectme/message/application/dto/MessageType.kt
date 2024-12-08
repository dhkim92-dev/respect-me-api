package kr.respectme.message.application.dto

enum class MessageType(
    val type: String
) {
    PLATFORM_ALARM("PLATFORM_ALARM"),
    GROUP_MESSAGE("GROUP_MESSAGE"),
    GROUP_INVITATION("GROUP_INVITATION"),
}