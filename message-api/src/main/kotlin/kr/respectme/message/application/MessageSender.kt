package kr.respectme.message.application

import kr.respectme.message.application.dto.GroupMessage
import kr.respectme.message.application.dto.MessageBody
import java.util.UUID

interface MessageSender {

    fun sendGroupMessage(receiveTargets: List<String>, message: GroupMessage): Boolean
}