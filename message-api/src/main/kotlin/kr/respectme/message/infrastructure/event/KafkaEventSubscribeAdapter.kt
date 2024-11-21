package kr.respectme.message.infrastructure.event

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.message.application.MessageSender
import kr.respectme.message.application.dto.GroupMessage
import kr.respectme.message.infrastructure.event.dto.NotificationCreateEvent
import kr.respectme.message.infrastructure.msa.MemberQueryPort
import kr.respectme.message.infrastructure.msa.dto.MemberQueryRequest
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaEventSubscribeAdapter(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val memberQueryPort: MemberQueryPort,
    private val messageSender: MessageSender,
    private val objectMapper: ObjectMapper
): EventSubscribePort {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val MESSAGE_MAX_LENGTH = 64

    @KafkaListener(topics = ["notification-create-event"], groupId = "respect-me")
    override fun onNotificationCreateEvent(event: NotificationCreateEvent): Boolean {
        logger.info("event received. ${objectMapper.writeValueAsString(event)}")
        val response = memberQueryPort.loadMembers(MemberQueryRequest(event.receiverIds))
        logger.info("member query response : ${objectMapper.writeValueAsString(response)}")
        logger.info("receiver count : ${response.data.count}")
        val deviceTokens: MutableList<String> = mutableListOf()
        response.data.data.filter { member -> member.blocked.not() }
            .forEach { member -> deviceTokens.addAll(member.deviceTokens) }
        deviceTokens.forEach { logger.info("message will be sent to device id : ${it}") }
        messageSender.sendGroupMessage(createGroupMessage(event, deviceTokens))

        return true
    }

    private fun createGroupMessage(event: NotificationCreateEvent, deviceTokens: List<String>)
    : GroupMessage {
        return GroupMessage(
            groupId = event.groupId,
            groupName = event.groupName,
            notificationId = event.notificationId,
            senderId = event.senderId,
            title = "New message from ${event.groupName}",
            body = if(event.contents.length < MESSAGE_MAX_LENGTH) event.contents else event.contents.substring(0, MESSAGE_MAX_LENGTH),
            targets = deviceTokens
        )
    }
}