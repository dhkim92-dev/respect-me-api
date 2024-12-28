package kr.respectme.auth.adapter.out.saga

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.auth.domain.MemberId
import kr.respectme.auth.port.`in`.saga.event.MemberDeleteSaga
import kr.respectme.auth.port.out.persistence.saga.AuthInfoDeleteSagaPublishPort
import kr.respectme.auth.port.out.persistence.saga.SagaDefinitions
import kr.respectme.common.saga.Saga
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class KafkaAuthInfoDeleteSagaPublishAdapter(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
): AuthInfoDeleteSagaPublishPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun publishAuthInfoDeleteCompletedSaga(transactionId: UUID, memberId: MemberId) {
        val message = Saga<MemberDeleteSaga>(
            transactionId = transactionId,
            timestamp = Instant.now().toEpochMilli(),
            data = MemberDeleteSaga(
                eventVersion = 1,
                memberId = memberId.id
            )
        )
        logger.debug("[member-delete-saga][auth-service-completed], transactionId : ${message.transactionId}")
        publishSaga(SagaDefinitions.MEMBER_DELETE_SAGA_AUTH_DELETE_COMPLETED, message)
    }

    override fun publishAuthInfoDeleteFailedSaga(transactionId: UUID, memberId: MemberId) {
        val message = Saga<MemberDeleteSaga>(
            transactionId = transactionId,
            data = MemberDeleteSaga(
                eventVersion = 1,
                memberId = memberId.id
            )
        )
        logger.debug("[member-delete-saga] publish auth-info-delete-failed-saga, transactionId : ${message.transactionId}")
        publishSaga(SagaDefinitions.MEMBER_DELETE_SAGA_AUTH_DELETE_FAILED, message)
    }

    @Retryable(
        value = [Exception::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000)
    )
    private fun publishSaga(topic: String, message: Saga<*>) {
        try {
            val record = ProducerRecord<String, String> (
                topic,
                message.transactionId.toString(),
                objectMapper.writeValueAsString(message)
            )
            kafkaTemplate.send(record)
        } catch(e: Exception) {
            logger.error("[member-delete-saga] failed to publish message to kafka topic : ${topic}, transactionId : ${message.transactionId}")
            throw e
        }
    }
}