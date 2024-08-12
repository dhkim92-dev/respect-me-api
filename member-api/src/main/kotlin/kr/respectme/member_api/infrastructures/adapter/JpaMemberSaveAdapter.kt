package kr.respectme.member_api.infrastructures.adapter

import kr.respectme.member_api.infrastructures.adapter.jpa.JpaMemberRepository
import kr.respectme.member_api.domain.mapper.MemberMapper
import kr.respectme.member_api.domain.model.Member
import kr.respectme.member_api.infrastructures.port.MemberSavePort
import org.springframework.stereotype.Component

@Component
class JpaMemberSaveAdapter(
    private val jpaMemberRepository: JpaMemberRepository,
    private val memberMapper: MemberMapper
): MemberSavePort {

    override fun save(member: Member): Member {
        val jpaEntity = jpaMemberRepository.save(memberMapper.toJpaEntity(member))
        return memberMapper.toDomainEntity(jpaEntity)
    }

    override fun delete(member: Member): Unit {
        return jpaMemberRepository.deleteById(member.id)
    }
}