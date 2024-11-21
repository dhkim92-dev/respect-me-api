package kr.respectme.message.application

import kr.respectme.message.application.dto.GroupMessage

interface MessageSender {

    fun sendGroupMessage(groupMessage: GroupMessage): Boolean
}