package kr.respectme.auth.adapter.out.saga

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.auth.domain.MemberId
import kr.respectme.auth.port.`in`.saga.event.MemberDeleteSaga
import kr.respectme.auth.port.out.persistence.saga.SagaEventPublishPort
import kr.respectme.auth.port.out.persistence.saga.SagaDefinitions
import kr.respectme.common.saga.SagaEvent
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Component
class KafkaSagaEventPublishAdapter(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
): SagaEventPublishPort {

    private val logger = LoggerFactory.getLogger(javaClass)


    @Retryable(
        value = [Exception::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000)
    )
    @Transactional
    override fun publish(eventName: String, event: SagaEvent<*>) {
        try {
            val record = ProducerRecord<String, String> (
                eventName,
                event.transactionId.toString(),
                objectMapper.writeValueAsString(event)
            )
            kafkaTemplate.send(record)
        } catch(e: Exception) {
            logger.error("[member-delete-saga] failed to publish message to kafka topic : ${eventName}, transactionId : ${event.transactionId}")
            throw e
        }
    }
}