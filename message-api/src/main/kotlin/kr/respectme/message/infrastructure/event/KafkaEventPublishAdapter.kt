package kr.respectme.message.infrastructure.event

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class KafkaEventPublishAdapter(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
): EventPublishPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun publish(eventName: String, event: Any): Boolean {
        try {
            val record = ProducerRecord<String, String>(
                eventName,
                UUID.randomUUID().toString(),
                objectMapper.writeValueAsString(event)
            )
            kafkaTemplate.send(record)
        } catch(e: Exception) {
            logger.error("publish kafka topic : ${eventName} publish failed.\n${e.message}")
            return false
        }
        return true
    }
}