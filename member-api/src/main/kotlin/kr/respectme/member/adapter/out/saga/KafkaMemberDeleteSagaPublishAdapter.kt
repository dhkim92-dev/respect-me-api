package kr.respectme.member.adapter.out.saga

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.saga.SagaEvent
import kr.respectme.member.port.out.event.SagaEventPublishPort
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class KafkaMemberDeleteSagaPublishAdapter(
    private val objectMapper: ObjectMapper,
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : SagaEventPublishPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun <T : SagaEvent<*>> publish(eventName: String, payload: T) {
        val producerRecord = ProducerRecord<String, String>(
            eventName,
            objectMapper.writeValueAsString(payload)
        )
        kafkaTemplate.send(producerRecord)
    }
}