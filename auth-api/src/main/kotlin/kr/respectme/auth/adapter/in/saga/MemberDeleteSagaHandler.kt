package kr.respectme.auth.adapter.`in`.saga

import kr.respectme.auth.domain.MemberAuthInfoRepository
import kr.respectme.auth.domain.MemberId
import kr.respectme.auth.port.`in`.saga.event.MemberDeleteSaga
import kr.respectme.common.saga.SagaEvent
import kr.respectme.common.saga.SagaEventHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberDeleteSagaHandler(
    private val authInfoRepository: MemberAuthInfoRepository
): SagaEventHandler<SagaEvent<MemberDeleteSaga>>() {

    @Transactional
    override fun handle(event: SagaEvent<MemberDeleteSaga>) {
        val memberAuthInfo = authInfoRepository.findByIdOrNull(MemberId.of(event.data!!.memberId))
            ?: throw IllegalArgumentException("Member not found")
        memberAuthInfo.setIsDeleted(true)
        authInfoRepository.save(memberAuthInfo)
    }

    @Transactional
    override fun compensate(event: SagaEvent<MemberDeleteSaga>) {
        val memberAuthInfo = authInfoRepository.findByIdOrNull(MemberId.of(event.data!!.memberId))
            ?: throw IllegalArgumentException("Member not found")
        memberAuthInfo.setIsDeleted(false)
        authInfoRepository.save(memberAuthInfo)
    }
}