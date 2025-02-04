package kr.respectme.member.adapter.`in`.saga

import kr.respectme.common.saga.SagaEvent
import kr.respectme.common.saga.SagaEventHandler
import kr.respectme.member.common.saga.event.MemberDeleteSaga
import kr.respectme.member.port.out.event.MemberDeleteTransactionRepository
import kr.respectme.member.port.out.persistence.command.MemberLoadPort
import kr.respectme.member.port.out.persistence.command.MemberSavePort
import org.springframework.stereotype.Service

@Service
class MemberDeleteSagaHandler(
    private val memberLoadPort: MemberLoadPort,
    private val memberSavePort: MemberSavePort
): SagaEventHandler<SagaEvent<MemberDeleteSaga>>() {

    override fun handle(event: SagaEvent<MemberDeleteSaga>) {
        // 멤버를 소프트 삭제하는 비지니스 로직
        val member = memberLoadPort.getMemberById(event.data!!.memberId)
            ?: throw IllegalArgumentException("Member not found")
        member.setSoftDeleted(true)
        memberSavePort.save(member)
    }

    override fun compensate(event: SagaEvent<MemberDeleteSaga>) {
        val member = memberLoadPort.getMemberById(event.data!!.memberId)
            ?: throw IllegalArgumentException("Member not found")
        member.setSoftDeleted(false)
        memberSavePort.save(member)
    }
}