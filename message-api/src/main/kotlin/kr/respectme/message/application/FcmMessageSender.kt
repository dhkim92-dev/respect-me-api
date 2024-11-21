package kr.respectme.message.application

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import kr.respectme.message.application.dto.GroupMessage
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
    private val eventPublishPort: EventPublishPort
): MessageSender {

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        private val NOTIFICATION_SENT_EVENT: String = "notification-sent-event"
    }

    override fun sendGroupMessage(groupMessage: GroupMessage): Boolean {
        val notifications = Notification.builder()
            .setTitle("New message from ${groupMessage.groupName}")
            .build()

        val multicastMessage = MulticastMessage.builder()
            .addAllTokens(groupMessage.targets)
            .setNotification(notifications)
            .build()

        return try {
            val responses = FirebaseMessaging.getInstance(app)
                .sendEachForMulticast(multicastMessage)
            responses.responses.forEachIndexed { index, response ->
                if(!response.isSuccessful()) {
                    logger.error("Failed to send message to ${groupMessage.targets[index]}")
                }
            }

            if(responses.successCount>0) {
                logger.info("Successs to sent message to group ${groupMessage.groupId}, success : ${responses.successCount} failure : ${responses.failureCount}")
                eventPublishPort.publish(NOTIFICATION_SENT_EVENT, NotificationSentEvent(
                    groupId = groupMessage.groupId,
                    notificationId = groupMessage.notificationId,
                    result = true,
                    successCount = responses.successCount,
                    failureCount = responses.failureCount
                ))
            } else {
                logger.error("Success to sent message request but 0 success.")
                eventPublishPort.publish(NOTIFICATION_SENT_EVENT, NotificationSentEvent(
                    groupId = groupMessage.groupId,
                    notificationId = groupMessage.notificationId,
                    result = false,
                    error = "Failed to sent message to group.",
                    successCount = responses.successCount,
                    failureCount = responses.failureCount
                ))
            }
            true
        } catch(e: Exception) {
            logger.error("Error sending FCM messages", e)
            false
        }
    }
}