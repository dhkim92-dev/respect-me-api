package kr.respectme.member.infrastructures.persistence.adapter

import kr.respectme.member.infrastructures.persistence.adapter.jpa.JpaMemberRepository
import kr.respectme.member.domain.mapper.MemberMapper
import kr.respectme.member.domain.model.Member
import kr.respectme.member.infrastructures.persistence.port.MemberLoadPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class JpaMemberLoadAdapter(
    private val jpaMemberRepository: JpaMemberRepository,
    private val memberMapper: MemberMapper,
): MemberLoadPort {


    override fun getMemberById(id: java.util.UUID): Member? {
        return jpaMemberRepository.findByIdOrNull(id)
            ?.let { memberMapper.toDomainEntity(it) }
    }

    override fun getMemberByEmail(email: String): Member? {
        return jpaMemberRepository.findByEmail(email)
            ?.let { memberMapper.toDomainEntity(it) }
    }

    override fun getMembersInList(UUIDIds: List<java.util.UUID>): List<Member> {
        return jpaMemberRepository.findAllById(UUIDIds)
            .map{ memberMapper.toDomainEntity(it) }
    }
}
