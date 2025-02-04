package kr.respectme.member.adapter.`in`.saga

import kr.respectme.common.saga.SagaEvent
import kr.respectme.member.adapter.out.persistence.jpa.JpaMemberRepository
import kr.respectme.member.common.saga.SagaEventDefinition
import kr.respectme.member.common.saga.event.MemberDeleteSaga
import kr.respectme.member.port.`in`.saga.MemberDeleteSagaEventListenPort
import kr.respectme.member.port.out.event.MemberDeleteTransaction
import kr.respectme.member.port.out.event.MemberDeleteTransactionRepository
import kr.respectme.member.port.out.event.SagaEventPublishPort
import kr.respectme.member.port.out.event.TransactionStatus
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 멤버 엔티티를 수정해야하는 이벤트를 구독하는 어댑터
 * @property memberLoadPort: MemberLoadPort
 * @property memberSavePort: MemberSavePort
 * @property memberDeleteTransactionRepository: MemberDeleteTransactionRepository
 * @property sagaEventPublishPort: MemberEventPublishPort
 */
@Component
class KafkaMemberDeleteSagaEventListenAdapter(
    private val memberDeleteTransactionRepository: MemberDeleteTransactionRepository,
    private val memberRepository: JpaMemberRepository,
    private val sagaEventPublishPort: SagaEventPublishPort,
    private val memberDeleteSagaHandler: MemberDeleteSagaHandler
): MemberDeleteSagaEventListenPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["member-delete-completed-saga"])
    @Transactional
    override fun onMemberDeleteCompleted(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            val transaction = getTransaction(event.transactionId)
            transaction.setStatus(TransactionStatus.COMPLETED)
            memberDeleteTransactionRepository.save(transaction)
            memberRepository.deleteById(transaction.getMemberId())
            logger.info("[member-delete-saga] - member delete completed ${event.transactionId}")
        } catch(e: Exception) {
            logger.error("[member-delete-saga-auth-service-completed][CRITICAL] - fail to process transaction: ${event.transactionId} memberId : ${event.data?.memberId}")
            // 여기에서까지 에러가 발생하는 경우 수동 개입이 필요하다.
        } finally {
            commit(acknowledgment)
        }
    }

    @KafkaListener(topics = ["member-delete-failed-saga"])
    @Transactional
    override fun onMemberDeleteFailed(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            val transaction = getTransaction(event.transactionId)
            transaction.setStatus(TransactionStatus.FAILED)
            memberDeleteTransactionRepository.save(transaction)
            memberDeleteSagaHandler.compensate(event)
        } catch(e: Exception) {
            logger.error("[member-delete-failed-saga-completed][CRITICAL] - fail to process transaction: ${event.transactionId} memberId : ${event.data?.memberId}")
            // 여기에서까지 에러가 발생하는 경우 수동 개입이 필요하다.
        } finally {
            commit(acknowledgment)
        }
    }

    /**
     * 그룹 서비스의 멤버 삭제 처리 성공 이벤트를 구독
     */
    @KafkaListener(topics = ["member-delete-saga-group-service-completed"])
    @Transactional
    override fun onGroupServiceCompleted(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            val transaction = getTransaction(event.transactionId)
            transaction.setGroupServiceCompleted(TransactionStatus.COMPLETED)
            memberDeleteTransactionRepository.save(transaction)
            logger.debug("[member-delete-saga] - group service completed ${event.transactionId}")
            if(checkAllTransactionCompleted(transaction)) {
                sagaEventPublishPort.publish(SagaEventDefinition.MEMBER_DELETE_COMPLETED_SAGA, event)
            }
        } catch(e: Exception) {
            logger.error("[member-delete-saga-group-service-completed][CRITICAL] - fail to process transaction: ${event.transactionId} memberId : ${event.data?.memberId}")
            sagaEventPublishPort.publish(SagaEventDefinition.MEMBER_DELETE_FAILED_SAGA, event)
        } finally {
            commit(acknowledgment)
        }
    }

    /**
     * 그룹 서비스의 멤버 삭제 실패 이벤트 구독
     */
    @KafkaListener(topics = ["member-delete-saga-group-service-failed"])
    @Transactional
    override fun onGroupServiceFailed(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            val transaction = getTransaction(event.transactionId)
            transaction.setGroupServiceCompleted(TransactionStatus.FAILED)
            memberDeleteTransactionRepository.save(transaction)

            if(transaction.getStatus() == TransactionStatus.PENDING) {
                sagaEventPublishPort.publish(SagaEventDefinition.MEMBER_DELETE_FAILED_SAGA, event)
            }
        } catch(e: Exception) {
            logger.error("[member-delete-saga-group-service-failed][CRITICAL] - fail to process transaction: ${event.transactionId} memberId : ${event.data?.memberId}")
            // 여기에서까지 에러가 발생하는 경우 수동 개입이 필요하다.
        } finally {
            commit(acknowledgment)
        }
    }

    /**
     * 인증 서비스의 멤버 삭제 성공 이벤트 구독
     */
    @KafkaListener(topics = ["member-delete-saga-auth-service-completed"])
    @Transactional
    override fun onAuthServiceCompleted(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            val transaction = getTransaction(event.transactionId)
            transaction.setAuthServiceCompleted(TransactionStatus.COMPLETED)
            memberDeleteTransactionRepository.save(transaction)
            logger.debug("[member-delete-saga] - auth service completed ${event.transactionId}")
            if(checkAllTransactionCompleted(transaction)) {
                sagaEventPublishPort.publish(SagaEventDefinition.MEMBER_DELETE_COMPLETED_SAGA, event)
            }
        } catch(e: Exception) {
            logger.error("[member-delete-saga-auth-service-completed][CRITICAL] - fail to process transaction: ${event.transactionId} memberId : ${event.data?.memberId}")
            sagaEventPublishPort.publish(SagaEventDefinition.MEMBER_DELETE_FAILED_SAGA, event)
        } finally {
            commit(acknowledgment)
        }
    }

    /**
     * 인증 서비스의 멤버 삭제 실패 이벤트 구독
     */
    @KafkaListener(topics = ["member-delete-saga-auth-service-failed"])
    @Transactional
    override fun onAuthServiceFailed(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            val transaction = getTransaction(event.transactionId)
            transaction.setAuthServiceCompleted(TransactionStatus.FAILED)
            memberDeleteTransactionRepository.save(transaction)

            if(transaction.getStatus() == TransactionStatus.PENDING) {
                sagaEventPublishPort.publish(SagaEventDefinition.MEMBER_DELETE_FAILED_SAGA, event)
            }
        } catch(e: Exception) {
            logger.error("[member-delete-saga-auth-service-failed][CRITICAL] - fail to process transaction: ${event.transactionId} memberId : ${event.data?.memberId}")
            // 여기에서까지 에러가 발생하는 경우 수동 개입이 필요하다.
        } finally {
            commit(acknowledgment)
        }
    }

    @Transactional(readOnly = false)
    private fun getTransaction(transactionId: UUID): MemberDeleteTransaction {
        return try {
            val transaction = memberDeleteTransactionRepository.findById(transactionId)
                ?: throw IllegalArgumentException("transaction not found")
            logger.debug("[member-delete-saga] - get transaction ${transactionId}, memeberId: ${transaction.getMemberId()}")
            transaction
        } catch (e: Exception) {
            logger.error("[member-delete-saga] - get transaction ${transactionId} failed + ${e.message}")
            throw e
        }
    }

    /**
     * 모든 트랜잭션이 완료되었는지 확인
     */
    private fun checkAllTransactionCompleted(transaction: MemberDeleteTransaction): Boolean {
        return transaction.getAuthServiceCompleted() == TransactionStatus.COMPLETED && transaction.getGroupServiceCompleted() == TransactionStatus.COMPLETED
    }

    private fun commit(acknowledgment: Acknowledgment) {
        acknowledgment.acknowledge()
    }
}