package kr.respectme.auth.adapter.`in`.saga

import kr.respectme.auth.domain.MemberAuthInfo
import kr.respectme.auth.domain.MemberAuthInfoRepository
import kr.respectme.auth.domain.MemberId
import kr.respectme.auth.port.`in`.saga.MemberDeleteSagaListenPort
import kr.respectme.auth.port.`in`.saga.event.MemberDeleteSaga
import kr.respectme.auth.port.out.persistence.saga.AuthInfoDeleteSagaPublishPort
import kr.respectme.common.saga.Saga
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.annotation.DltHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class KafkaMemberDeleteSagaListenAdapter(
    private val authInfoRepository: MemberAuthInfoRepository,
    private val authInfoDeleteSagaPublishPort: AuthInfoDeleteSagaPublishPort,
): MemberDeleteSagaListenPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics=["member-delete-saga"])
    @Transactional
    override fun onMemberDelete(message: Saga<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        logger.debug("[member-delete-saga] - delete member auth info started")
        try {
            val authInfo = getAuthInfo(message)
            authInfo.setIsDeleted(true)
            authInfoRepository.save(authInfo)
            authInfoDeleteSagaPublishPort.publishAuthInfoDeleteCompletedSaga(
                transactionId = message.transactionId,
                memberId = MemberId(message.data!!.memberId)
            )
            commit(acknowledgment)
        } catch(e: Exception) {
            logger.error("[member-delete-saga] - delete member auth info failed")
            authInfoDeleteSagaPublishPort.publishAuthInfoDeleteFailedSaga(
                transactionId = message.transactionId,
                memberId = MemberId(message.data!!.memberId)
            )
            throw e
        }
    }

    @RetryableTopic(
        attempts = "3",
        backoff = Backoff(delay = 1000),
        autoCreateTopics = "false",
    )
    @KafkaListener(topics=["member-delete-failed-saga"])
    @Transactional
    override fun onMemberDeleteFailed(message: Saga<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        logger.info("[member-delete-failed-saga] transaction: ${message.transactionId} - rollback delete member auth info started")
        try {
            val authInfo = getAuthInfo(message)
            authInfo.setIsDeleted(false)
            authInfoRepository.save(authInfo)
            commit(acknowledgment)
            logger.info("[member-delete-failed-saga] - rollback delete member auth info completed")
        } catch(e: Exception) {
            logger.error("[member-delete-failed-saga] - rollback delete member auth info failed")
            throw e
        }
    }

    @RetryableTopic(
        attempts = "3",
        backoff = Backoff(delay = 1000),
        autoCreateTopics = "false",
    )
    @KafkaListener(topics=["member-delete-completed-saga"])
    @Transactional
    override fun onMemberDeleteCompleted(message: Saga<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        logger.info("[member-delete-completed-saga] - transactionId: ${message.transactionId} delete member auth info started")
        try {
            val authInfo = getAuthInfo(message)
            authInfoRepository.delete(authInfo)
            logger.info("[member-delete-completed-saga] - transactionId: ${message.transactionId} delete member auth info completed")
            commit(acknowledgment)
        } catch(e: Exception) {
            logger.error("[member-delete-completed-saga] - delete member auth info failed.")
            throw e
        }
    }

    @DltHandler
    fun handleDlt(message: Saga<MemberDeleteSaga>,
                  @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
                  @Header(KafkaHeaders.KEY) key: String,
                  acknowledgment: Acknowledgment
    ) {
        logger.error("[${topic}][FATAL] - transactionId: ${message.transactionId} - ${message.data} failed to process. handle it manually.")
    }

    private fun getAuthInfo(message: Saga<MemberDeleteSaga>): MemberAuthInfo {
        val memberId = MemberId.of(message.data!!.memberId)
        return authInfoRepository.findByIdOrNull(memberId)
            ?: throw IllegalStateException("MemberAuthInfo not found. memberId: $memberId")
    }

    private fun commit(acknowledgment: Acknowledgment) {
        acknowledgment.acknowledge()
    }
}