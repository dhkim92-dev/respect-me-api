package kr.respectme.group.adapter.out.event

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.port.out.event.EventSubscribePort
import kr.respectme.group.port.out.event.dto.NotificationSentEvent
import kr.respectme.group.port.out.persistence.LoadGroupPort
import kr.respectme.group.port.out.persistence.SaveGroupPort
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

    @KafkaListener(topics = ["notification-sent-event"])
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

        val notification = group.getNotifications().find { it.id == event.notificationId }

        if(notification == null) {
            logger.error("notification-sent-event handle failed. notification not found, notification id : ${event.notificationId}")
            return
        }

        notification.updateStatus(
            if(event.result) NotificationStatus.PUBLISHED
            else NotificationStatus.FAILED
        )

        logger.debug("notification status updated : ${notification.toString()}")

        group.addNotification(notification.id ,notification)
        
        saveGroupPort.save(group)
    }
}