package kr.respectme.message.infrastructure.event

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaEventPublishAdapter(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
): EventPublishPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun publish(eventName: String, event: Any): Boolean {
        try {
            kafkaTemplate.send(eventName, objectMapper.writeValueAsString(event))
        } catch(e: Exception) {
            logger.error("publish kafka topic : ${eventName} publish failed.\n${e.message}")
            return false
        }
        return true
    }
}