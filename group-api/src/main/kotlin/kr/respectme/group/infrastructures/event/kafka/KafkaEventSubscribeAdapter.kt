package kr.respectme.group.infrastructures.event.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import kr.respectme.common.error.NotFoundException
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.domain.notifications.Notification
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.infrastructures.event.EventSubscribePort
import kr.respectme.group.infrastructures.event.dto.NotificationSentEvent
import kr.respectme.group.infrastructures.persistence.port.LoadGroupPort
import kr.respectme.group.infrastructures.persistence.port.SaveGroupPort
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaEventSubscribeAdapter(
    private val saveGroupPort: SaveGroupPort,
    private val loadGroupPort: LoadGroupPort,
    private val objectMapper: ObjectMapper
): EventSubscribePort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["notification-sent-event"], groupId="respect-me")
    @Transactional
    override fun onReceiveNotificationSent(event: NotificationSentEvent) {
        logger.info("push-notification-success: ${objectMapper.writeValueAsString(event)}")
        val group = loadGroupPort.loadGroup(event.groupId)

        if(group === null) {
            logger.error("notification-sent-event handling failed. target group : ${event.groupId} reason: group not exists.")
            return
        }

        logger.debug("event target notification id : ${event.notificationId}")
        logger.debug("notification status : ${event.result}")

        val notification = group.notifications.find { it.id == event.notificationId }

        if(notification == null) {
            logger.error("notification-sent-event handle failed. notification not found, notification id : ${event.notificationId}")
            return
        }

        notification.updateStatus(
            if(event.result) NotificationStatus.PUBLISHED
            else NotificationStatus.FAILED
        )

        logger.debug("notification status updated : ${notification.toString()}")

        group.addNotification(notification.senderId ,notification)
        
        saveGroupPort.save(group)
    }
}