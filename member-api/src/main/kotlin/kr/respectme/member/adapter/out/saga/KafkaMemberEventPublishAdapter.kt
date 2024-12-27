package kr.respectme.member.adapter.out.saga

import kr.respectme.common.saga.Saga
import kr.respectme.member.common.saga.SagaEventDefinition
import kr.respectme.member.common.saga.event.MemberDeleteSaga
import kr.respectme.member.port.out.saga.MemberDeleteTransaction
import kr.respectme.member.port.out.saga.MemberDeleteTransactionRepository
import kr.respectme.member.port.out.saga.MemberEventPublishPort
import kr.respectme.member.port.out.saga.TransactionStatus
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class KafkaMemberEventPublishAdapter(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val memberDeleteTransactionRepository: MemberDeleteTransactionRepository,
): MemberEventPublishPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun memberDeleteSagaStart(saga: MemberDeleteSaga) {
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
        logger.debug("[MemberDeleteSaga - ${transaction.getId()}] transaction started")
        kafkaTemplate.send(SagaEventDefinition.MEMBER_DELETE_SAGA, message)
    }

    override fun memberDeleteSagaCompleted(transactionId: UUID, saga: MemberDeleteSaga) {
        val message = Saga(
            transactionId = transactionId,
            timestamp = Instant.now().toEpochMilli(),
            data = saga,
        )

        logger.debug("[MemberDeleteSaga - ${transactionId}] transaction completed")
        kafkaTemplate.send(SagaEventDefinition.MEMBER_DELETE_COMPLETED_SAGA, message)
    }

    override fun memberDeleteSagaFailed(transactionId: UUID, saga: MemberDeleteSaga) {
        val message = Saga<MemberDeleteSaga>(
            transactionId = transactionId,
            timestamp = Instant.now().toEpochMilli(),
            data = saga,
        )

        logger.debug("[MemberDeleteSaga - ${transactionId}] transaction failed")
        kafkaTemplate.send(SagaEventDefinition.MEMBER_DELETE_FAILED_SAGA, message)
    }
}