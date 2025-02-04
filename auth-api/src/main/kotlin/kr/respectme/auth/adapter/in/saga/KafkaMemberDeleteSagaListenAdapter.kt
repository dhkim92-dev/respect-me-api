package kr.respectme.auth.adapter.`in`.saga

import kr.respectme.auth.domain.MemberAuthInfo
import kr.respectme.auth.domain.MemberAuthInfoRepository
import kr.respectme.auth.domain.MemberId
import kr.respectme.auth.port.`in`.saga.MemberDeleteSagaListenPort
import kr.respectme.auth.port.`in`.saga.event.MemberDeleteSaga
import kr.respectme.auth.port.out.persistence.saga.SagaDefinitions
import kr.respectme.auth.port.out.persistence.saga.SagaEventPublishPort
import kr.respectme.common.saga.SagaEvent
import kr.respectme.common.saga.SagaEventHandler
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.support.Acknowledgment
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class KafkaMemberDeleteSagaListenAdapter(
    private val authInfoRepository: MemberAuthInfoRepository,
    private val authInfoDeleteSagaPublishPort: SagaEventPublishPort,
    private val memberDeleteSagaHandler: SagaEventHandler<SagaEvent<MemberDeleteSaga>>
): MemberDeleteSagaListenPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics=["member-delete-saga"])
    @Transactional
    override fun onMemberDelete(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            logger.info("[member-delete-saga] received message : $message")
            memberDeleteSagaHandler.handleEvent(message)
            authInfoDeleteSagaPublishPort.publish(SagaDefinitions.MEMBER_DELETE_SAGA_AUTH_DELETE_COMPLETED, message)
        } catch(e: Exception) {
            logger.error("[member-delete-saga] failed to handle message : $message", e)
            // 여기서 메시지를 발행해야한다.
            authInfoDeleteSagaPublishPort.publish(SagaDefinitions.MEMBER_DELETE_SAGA_AUTH_DELETE_FAILED, message)
        } finally {
            commit(acknowledgment)
        }
    }

    @KafkaListener(topics=["member-delete-failed-saga"])
    @Transactional
    override fun onMemberDeleteFailed(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            logger.info("[member-delete-failed-saga] received message : $message")
            memberDeleteSagaHandler.compensate(message)
        } catch(e: Exception) {
            logger.error("[member-delete-failed-saga][CRITICAL] failed to handle message : $message", e)
        } finally {
            commit(acknowledgment)
        }
    }

    @KafkaListener(topics=["member-delete-completed-saga"])
    @Transactional
    override fun onMemberDeleteCompleted(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            logger.info("[member-delete-completed-saga] received message : $message")
            authInfoRepository.deleteById(MemberId.of(message.data!!.memberId))
        } catch(e: Exception) {
            logger.error("[member-delete-completed-saga][CRITICAL] failed to handle message : $message", e)
        } finally {
            commit(acknowledgment)
        }
    }

    @Transactional
    private fun getAuthInfo(message: SagaEvent<MemberDeleteSaga>): MemberAuthInfo {
        val memberId = MemberId.of(message.data!!.memberId)
        return authInfoRepository.findByIdOrNull(memberId)
            ?: throw IllegalStateException("MemberAuthInfo not found. memberId: $memberId")
    }

    @Transactional
    private fun commit(acknowledgment: Acknowledgment) {
        acknowledgment.acknowledge()
    }
}