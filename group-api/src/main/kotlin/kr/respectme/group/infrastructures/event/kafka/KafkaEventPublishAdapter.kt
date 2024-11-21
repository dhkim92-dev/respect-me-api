package kr.respectme.group.infrastructures.event.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.group.infrastructures.event.EventPublishPort
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaEventPublishAdapter(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
): EventPublishPort {

    override fun publish(eventName: String, message: Any) {
        kafkaTemplate.send(eventName, objectMapper.writeValueAsString(message))
    }
}