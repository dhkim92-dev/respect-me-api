package kr.respectme.member.adapter.out.persistence

import kr.respectme.member.adapter.out.persistence.jpa.JpaMemberRepository
import kr.respectme.member.domain.mapper.MemberMapper
import kr.respectme.member.domain.model.Member
import kr.respectme.member.port.out.persistence.command.MemberLoadPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class JpaMemberLoadAdapter(
    private val jpaMemberRepository: JpaMemberRepository,
    private val memberMapper: MemberMapper,
): MemberLoadPort {


    override fun getMemberById(id: UUID): Member? {
        return jpaMemberRepository.findByIdOrNull(id)
            ?.let { memberMapper.toDomainEntity(it) }
    }

    override fun getMemberByEmail(email: String): Member? {
        return jpaMemberRepository.findByEmail(email)
            ?.let { memberMapper.toDomainEntity(it) }
    }

    override fun getMembersInList(memberIds: List<UUID>): List<Member> {
        return jpaMemberRepository.findByIds(memberIds)
            .map{ memberMapper.toDomainEntity(it) }
    }

    override fun getMemberWithDeviceToken(memberId: UUID): Member? {
        return jpaMemberRepository.findByIdWithDeviceTokens(memberId)
            ?.let { memberMapper.toDomainEntity(it) }
    }
}
