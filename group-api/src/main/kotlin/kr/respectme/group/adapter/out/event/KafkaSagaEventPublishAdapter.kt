package kr.respectme.group.adapter.out.saga

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.saga.SagaEvent
import kr.respectme.group.application.event.MemberDeleteSaga
import kr.respectme.group.common.saga.SagaDefinitions
import kr.respectme.group.port.out.saga.SagaEventPublishPort
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class KafkaMemberDeleteSagaPublishAdapter(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
): SagaEventPublishPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun publishMemberDeleteSagaGroupServiceCompleted(message: SagaEvent<MemberDeleteSaga>) {
        publishSaga(SagaDefinitions.MEMBER_DELETE_SAGA_GROUP_SERVICE_COMPLETED, message)
    }

    @Transactional
    override fun publishMemberDeleteSagaGroupServiceFailed(message: SagaEvent<MemberDeleteSaga>) {
        publishSaga(SagaDefinitions.MEMBER_DELETE_SAGA_GROUP_SERVICE_FAILED, message)
    }

    @Transactional
    private fun publishSaga(topic: String, saga: SagaEvent<MemberDeleteSaga>) {
        try {
            val record = ProducerRecord(
                topic,
                saga.transactionId.toString(),
                objectMapper.writeValueAsString(saga)
            )
            kafkaTemplate.send(record)
            logger.info("[${topic}] published message to kafka, transactionId : ${saga.transactionId}")
        } catch(e: Exception) {
            logger.error("[${topic}] failed to publish message to kafka, transactionId : ${saga.transactionId}")
            throw e
        }
    }
}