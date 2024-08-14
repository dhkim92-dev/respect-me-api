package kr.respectme.member.infrastructures.adapter

import kr.respectme.member.infrastructures.adapter.jpa.JpaMemberRepository
import kr.respectme.member.domain.mapper.MemberMapper
import kr.respectme.member.domain.model.Member
import kr.respectme.member.infrastructures.port.MemberSavePort
import org.springframework.stereotype.Component

@Component
class JpaMemberSaveAdapter(
    private val jpaMemberRepository: JpaMemberRepository,
    private val memberMapper: MemberMapper
): MemberSavePort {

    override fun save(UUID: Member): Member {
        val jpaEntity = jpaMemberRepository.save(memberMapper.toJpaEntity(UUID))
        return memberMapper.toDomainEntity(jpaEntity)
    }

    override fun delete(UUID: Member): Unit {
        return jpaMemberRepository.deleteById(UUID.id)
    }
}