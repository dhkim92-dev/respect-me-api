package kr.respectme.member.adapter.out.persistence

import kr.respectme.member.adapter.out.persistence.jpa.JpaMemberRepository
import kr.respectme.member.domain.mapper.MemberMapper
import kr.respectme.member.domain.model.Member
import kr.respectme.member.port.out.persistence.command.MemberSavePort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class JpaMemberSaveAdapter(
    private val jpaMemberRepository: JpaMemberRepository,
    private val memberMapper: MemberMapper
): MemberSavePort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun save(member: Member): Member {
        logger.debug("member save adapter save")
        val jpaMemberEntity = memberMapper.toJpaEntity(member)
        logger.debug("member save adapter save jpaMemberEntity: $jpaMemberEntity")
        val jpaEntity = jpaMemberRepository.save(memberMapper.toJpaEntity(member))
        return memberMapper.toDomainEntity(jpaEntity)
    }

    override fun delete(member: Member): Unit {
        return jpaMemberRepository.deleteById(member.id)
    }
}