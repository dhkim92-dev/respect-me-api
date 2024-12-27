package kr.respectme.member.adapter.out.saga

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.saga.Saga
import kr.respectme.member.common.saga.SagaEventDefinition
import kr.respectme.member.common.saga.event.MemberDeleteSaga
import kr.respectme.member.port.out.saga.MemberDeleteTransaction
import kr.respectme.member.port.out.saga.MemberDeleteTransactionRepository
import kr.respectme.member.port.out.saga.MemberDeleteSagaEventPublishPort
import kr.respectme.member.port.out.saga.TransactionStatus
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Component
class KafkaMemberDeleteSagaPublishAdapter(
    private val objectMapper: ObjectMapper,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val memberDeleteTransactionRepository: MemberDeleteTransactionRepository,
): MemberDeleteSagaEventPublishPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun memberDeleteSagaStart(saga: MemberDeleteSaga) {

        logger.info("[member-delete-saga] - start member delete saga for member ${saga.memberId}")
        var transaction = MemberDeleteTransaction(
            memberId = saga.memberId,
            status = TransactionStatus.PENDING,
            authServiceStatus = TransactionStatus.PENDING,
            groupServiceStatus = TransactionStatus.PENDING
        )

        transaction = memberDeleteTransactionRepository.save(transaction)

        val message = Saga(
            transactionId = transaction.getId(),
            timestamp = Instant.now().toEpochMilli(),
            data = saga,
        )

        logger.info("[member-delete-saga] - transaction created for member ${saga.memberId}")
        publishSaga(SagaEventDefinition.MEMBER_DELETE_SAGA, message)
    }

    override fun memberDeleteSagaCompleted(transactionId: UUID, saga: MemberDeleteSaga) {
        val message = Saga(
            transactionId = transactionId,
            timestamp = Instant.now().toEpochMilli(),
            data = saga,
        )
        logger.info("[member-delete-saga][completed][${transactionId}] - transaction completed for member ${saga.memberId}")
        publishSaga(SagaEventDefinition.MEMBER_DELETE_COMPLETED_SAGA, message)
    }

    override fun memberDeleteSagaFailed(transactionId: UUID, saga: MemberDeleteSaga) {
        val message = Saga<MemberDeleteSaga>(
            transactionId = transactionId,
            timestamp = Instant.now().toEpochMilli(),
            data = saga,
        )
        logger.info("[member-delete-saga][failed][${transactionId}] - transaction failed for member ${saga.memberId}")
        publishSaga(SagaEventDefinition.MEMBER_DELETE_FAILED_SAGA, message)
    }

    private fun publishSaga(eventName: String, message: Saga<MemberDeleteSaga>) {
        val record = ProducerRecord<String, String> (
            eventName,
            message.transactionId.toString(),
            objectMapper.writeValueAsString(message)
        )
        kafkaTemplate.send(record)
    }
}