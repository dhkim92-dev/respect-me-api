package kr.respectme.message.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import kr.respectme.message.application.dto.GroupMessage
import kr.respectme.message.application.dto.MessageBody
import kr.respectme.message.application.dto.MessageType
import kr.respectme.message.infrastructure.event.EventPublishPort
import kr.respectme.message.infrastructure.event.dto.NotificationSentEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Message Sender Implementation using FCM
 * @param app FirebaseApp
 */
@Service
class FcmMessageSender(
    private val app: FirebaseApp,
    private val eventPublishPort: EventPublishPort,
    private val objectMapper: ObjectMapper
): MessageSender {

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        private val NOTIFICATION_SENT_EVENT: String = "notification-sent-event"
    }

    override fun sendGroupMessage(receiveTargets: List<String>, message: GroupMessage): Boolean {
        if(receiveTargets.isEmpty()) {
            logger.error("No device tokens to send message for group ${message.groupId}")
            return false
        }

        val messageBody = MessageBody(
            type = MessageType.GROUP_MESSAGE,
            title = message.title,
            body = message.contents
        )

        val notifications = Notification.builder()
            .setTitle(messageBody.title)
            .setImage(message.groupImageUrl)
            .setBody(messageBody.body)
            .build()

        val multicastMessage = MulticastMessage.builder()
            .addAllTokens(receiveTargets)
            .setNotification(notifications)
            .putData("messageType", messageBody.type.type)
            .putData("groupId", message.groupId.toString())
            .putData("notificationId", message.notificationId.toString())
            .build()

        return try {
            val responses = FirebaseMessaging.getInstance(app)
                .sendEachForMulticast(multicastMessage)
            responses.responses.forEachIndexed { index, response ->
                if(!response.isSuccessful()) {
                    logger.error("Failed to send message to ${receiveTargets[index]}")
                }
            }

            val notificationSentEvent = NotificationSentEvent(
                groupId = message.groupId,
                notificationId = message.notificationId,
                result = responses.successCount > 0,
                successCount = responses.successCount,
                failureCount = responses.failureCount
            )

            if(responses.successCount>0) {
                logger.debug("Success to sent message to group ${message.groupId}, " +
                        "success : ${responses.successCount} " +
                        "failure : ${responses.failureCount}"
                )
            } else {
                notificationSentEvent.error = "Failed to sent message to group."
            }

            eventPublishPort.publish(NOTIFICATION_SENT_EVENT, notificationSentEvent)
            true
        } catch(e: Exception) {
            logger.error("Error sending FCM messages ${e.stackTraceToString()}")
            false
        }
    }
}