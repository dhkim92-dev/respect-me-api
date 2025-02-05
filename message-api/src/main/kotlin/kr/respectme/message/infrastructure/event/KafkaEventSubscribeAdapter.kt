package kr.respectme.message.infrastructure.event

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.message.application.MessageSender
import kr.respectme.message.application.dto.GroupMessage
import kr.respectme.message.application.dto.MessageBody
import kr.respectme.message.application.dto.MessageType
import kr.respectme.message.infrastructure.event.dto.NotificationCreateEvent
import kr.respectme.message.infrastructure.msa.MemberQueryPort
import kr.respectme.message.infrastructure.msa.dto.MemberQueryRequest
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class KafkaEventSubscribeAdapter(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val memberQueryPort: MemberQueryPort,
    private val messageSender: MessageSender,
    private val objectMapper: ObjectMapper
): EventSubscribePort {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val MESSAGE_TITLE_MAX_LENGTH = 22
    private val MESSAGE_BODY_MAX_LENGTH = 100

    @KafkaListener(topics = ["notification-create-event"])
    @Transactional
    override fun onNotificationCreateEvent(event: NotificationCreateEvent): Boolean {
        logger.info("Group Notification Create Event Received: \n" +
                "groupId: ${event.groupId}\n" +
                "groupName: ${event.groupName}\n"+
                "senderId: ${event.senderId}\n"
        )

        event.receiverIds.forEach { receiverId ->
            logger.info("receiverId: $receiverId")
        }

        val response = memberQueryPort.loadMembers(MemberQueryRequest(event.receiverIds))
        val deviceTokens: MutableSet<String> = mutableSetOf()
        response.data.data.filter { member -> member.isBlocked.not() }
            .forEach { member -> deviceTokens.addAll(member.deviceTokens) }
        val groupMessage = createGroupMessage(event)

        messageSender.sendGroupMessage(
            receiveTargets = deviceTokens.toList(),
            message = groupMessage
        )

        return true
    }

    private fun createGroupMessage(event: NotificationCreateEvent)
    : GroupMessage {
        return GroupMessage(
            groupId = event.groupId,
            groupName = event.groupName,
            groupImageUrl = event.groupImageUrl,
            title = event.groupName.take(MESSAGE_TITLE_MAX_LENGTH),
            contents = event.contents.take(MESSAGE_BODY_MAX_LENGTH),
            notificationId = event.notificationId,
            senderId = event.senderId,
        )
    }
}