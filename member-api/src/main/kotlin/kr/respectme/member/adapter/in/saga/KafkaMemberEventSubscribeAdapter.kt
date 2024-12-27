package kr.respectme.member.adapter.`in`.saga

import kr.respectme.common.saga.Saga
import kr.respectme.member.common.saga.event.MemberDeleteSaga
import kr.respectme.member.port.`in`.saga.MemberEventSubscribePort
import kr.respectme.member.port.out.persistence.command.MemberLoadPort
import kr.respectme.member.port.out.persistence.command.MemberSavePort
import kr.respectme.member.port.out.saga.MemberDeleteTransaction
import kr.respectme.member.port.out.saga.MemberDeleteTransactionRepository
import kr.respectme.member.port.out.saga.MemberEventPublishPort
import kr.respectme.member.port.out.saga.TransactionStatus
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.support.Acknowledgment
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * 멤버 엔티티를 수정해야하는 이벤트를 구독하는 어댑터
 * @property memberLoadPort: MemberLoadPort
 * @property memberSavePort: MemberSavePort
 * @property memberDeleteTransactionRepository: MemberDeleteTransactionRepository
 * @property memberEventPublishPort: MemberEventPublishPort
 */
@Component
class KafkaMemberEventSubscribeAdapter(
    private val memberLoadPort: MemberLoadPort,
    private val memberSavePort: MemberSavePort,
    private val memberDeleteTransactionRepository: MemberDeleteTransactionRepository,
    private val memberEventPublishPort: MemberEventPublishPort
): MemberEventSubscribePort {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 멤버 탈퇴 사가 트랜잭션이 완료되었을 경우 실행할 로직, 소프트 삭제인 상태의 엔티티를 실제로 제거한다.
     * 이 때 연관된 객체들까지 모두 제거된다.
     * @param message: Saga<MemberDeleteSaga>
     */
    @RetryableTopic(
        attempts = "3",
        backoff = Backoff(delay = 1000),
        autoCreateTopics = "false",
    )
    @KafkaListener(topics = ["member-delete-completed-saga"])
    override fun onMemberDeleteSagaCompleted(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    ) {
        try {
            val member = memberLoadPort.getMemberById(message.data?.memberId!!)

            // 멤버가 이미 삭제된 경우라면 문제가 되지 않는다.
            if(member != null) {
                memberSavePort.delete(member)
            }

            logger.info("[member-delete-completed-saga] - delete member ${message.data?.memberId} completed")
            commit(acknowledgment)
        } catch(e: Exception) {
            logger.error("[member-delete-completed-saga] - delete member ${message.data?.memberId} failed")
            throw e
        }
    }

    /**
     * 멤버 탈퇴 사가 트랜잭션이 실패했을 경우 실행할 로직, 소프트 삭제 상태인 엔티티를 원상 복구 시킨다.
     * @param message: Saga<MemberDeleteSaga>
     * @apram acknowledgment: Acknowledgment
     */
    @RetryableTopic(
        attempts = "3",
        backoff = Backoff(delay = 1000),
        autoCreateTopics = "false",
    )
    @KafkaListener(topics = ["member-delete-failed-saga"])
    override fun onMemberDeleteSagaFailed(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    ) {
        try {
            compensation(message)
            logger.info("[member-delete-failed-saga] - restore member ${message.data?.memberId} completed")
            commit(acknowledgment)
        } catch(e: Exception) {
            logger.error("[member-delete-failed-saga] - restore member ${message.data?.memberId} failed")
            throw e
        }
    }

    /**
     * 멤버 탈퇴 사가 트랜잭션이 그룹 서비스에서 완료되면 실행할 로직.
     * @param message: Saga<MemberDeleteSaga>
     */
    @RetryableTopic(
        attempts = "3",
        backoff = Backoff(delay = 1000),
        autoCreateTopics = "false",
    )
    @KafkaListener(topics = ["member-delete-saga-group-service-completed"])
    override fun onMemberDeleteSagaGroupServiceComplete(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    ) {
        try {
            var transaction = getTransaction(message.transactionId)
            transaction.setGroupServiceCompleted(TransactionStatus.COMPLETED)

            // 현재 연관된 서비스들의 상태가 모두 완료되어있으나, 현재 트랜잭션이 아직 완료되지 않은 상태라면
            if(checkAllTransactionCompleted(transaction) &&
                (transaction.getStatus() == TransactionStatus.PENDING)) {
                transaction.setStatus(TransactionStatus.COMPLETED)
                memberEventPublishPort.memberDeleteSagaCompleted(transaction.getId(), message.data!!)
            }

            transaction = memberDeleteTransactionRepository.save(transaction)
            commit(acknowledgment)
        } catch(e: Exception) {
            logger.error("[member-delete-saga][group-service-completed] - transaction ${message.transactionId} failed")
            throw e
        }
    }

    /**
     * 멤버 탈퇴 사가 트랜잭션이 인증 서비스에서 완료되면 실행할 로직.
     */
    @RetryableTopic(
        attempts = "3",
        backoff = Backoff(delay = 1000),
        autoCreateTopics = "false",
    )
    @KafkaListener(topics=["member-delete-saga-auth-service-completed"])
    override fun onMemberDeleteSagaAuthServiceCompleted(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    ) {
        try {
            var transaction = getTransaction(message.transactionId)
            transaction.setAuthServiceCompleted(TransactionStatus.COMPLETED)

            // 현재 연관된 서비스들의 상태가 모두 완료되어있으나, 현재 트랜잭션이 아직 완료되지 않은 상태라면
            if(checkAllTransactionCompleted(transaction) &&
                (transaction.getStatus() == TransactionStatus.PENDING)) {

                transaction.setStatus(TransactionStatus.COMPLETED)
                memberEventPublishPort.memberDeleteSagaCompleted(transaction.getId(), message.data!!)
            }

            transaction = memberDeleteTransactionRepository.save(transaction)
            commit(acknowledgment)
        } catch(e: Exception) {
            logger.error("[member-delete-saga][auth-service-completed] - transaction ${message.transactionId} failed")
            throw e
        }
    }

    /**
     * 멤버 탈퇴 사가 트랜잭션이 그룹 서비스에서 실패하면 실행할 로직.
     */
    @RetryableTopic(
        attempts = "3",
        backoff = Backoff(delay = 1000),
        autoCreateTopics = "false",
    )
    @KafkaListener(topics = ["member-delete-saga-group-service-failed"])
    override fun onMemberDeleteSagaGroupServiceFailed(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    ) {
        try {
            var transaction = getTransaction(message.transactionId)
            transaction.setGroupServiceCompleted(TransactionStatus.FAILED)
            transaction.setStatus(TransactionStatus.FAILED)
            transaction = memberDeleteTransactionRepository.save(transaction)
            memberEventPublishPort.memberDeleteSagaFailed(transaction.getId(), message.data!!)
            commit(acknowledgment)
        } catch(e: Exception) {
            logger.error("[member-delete-saga][group-service-failed] - transaction ${message.transactionId} handling failed")
            throw e
        }

    }

    /**
     * 멤버 탈퇴 사가 트랜잭션이 인증 서비스에서 실패하면 실행할 로직.
     */
    @RetryableTopic(
        attempts = "3",
        backoff = Backoff(delay = 1000),
        autoCreateTopics = "false",
    )
    @KafkaListener(topics = ["member-delete-saga-auth-service-failed"])
    override fun onMemberDeleteSagaAuthServiceFailed(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    ) {
        try {
            var transaction = getTransaction(message.transactionId)
            transaction.setAuthServiceCompleted(TransactionStatus.FAILED)
            transaction.setStatus(TransactionStatus.FAILED)
            transaction = memberDeleteTransactionRepository.save(transaction)
            memberEventPublishPort.memberDeleteSagaFailed(transaction.getId(), message.data!!)
            commit(acknowledgment)
        } catch(e: Exception) {
            logger.error("[member-delete-saga][auth-service-failed] - transaction ${message.transactionId} handling failed")
            throw e
        }
    }

    private fun getTransaction(transactionId: UUID): MemberDeleteTransaction {
        return try {
            memberDeleteTransactionRepository.findByIdForUpdate(transactionId)
                ?: throw IllegalArgumentException("transaction not found")
        } catch (e: Exception) {
            logger.error("[MemberDeleteSaga][Fatal] - transaction ${transactionId} should be restored manually failed")
            throw e
        }
    }

    /**
     * 멤버 삭제 사가 트랜잭션 실패 시 보상 로직
     */
    private fun compensation(message: Saga<MemberDeleteSaga>) {
        logger.info("[MemberDeleteSaga][Compensation] - member ${message.data?.memberId} should be restored")
        try {
            val memberId = message.data?.memberId
            memberLoadPort.getMemberById(memberId!!)?.let { member ->
                member.setSoftDeleted(false)
                memberSavePort.save(member)
            }
        } catch(e: Exception) {
            logger.error("[MemberDeleteSaga][Fatal] - member ${message.data?.memberId} should be restored manually failed")
            throw e
        }
    }

    private fun checkAllTransactionCompleted(transaction: MemberDeleteTransaction): Boolean {
        return transaction.getAuthServiceCompleted() == TransactionStatus.COMPLETED && transaction.getGroupServiceCompleted() == TransactionStatus.COMPLETED
    }

    private fun commit(acknowledgment: Acknowledgment) {
        acknowledgment.acknowledge()
    }
}