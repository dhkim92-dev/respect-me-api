package kr.respectme.member.infrastructures.persistence.adapter

import kr.respectme.member.infrastructures.persistence.adapter.jpa.JpaMemberRepository
import kr.respectme.member.domain.mapper.MemberMapper
import kr.respectme.member.domain.model.Member
import kr.respectme.member.infrastructures.persistence.port.command.MemberSavePort
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
        val jpaEntity = jpaMemberRepository.save(memberMapper.toJpaEntity(member))
        return memberMapper.toDomainEntity(jpaEntity)
    }

    override fun delete(member: Member): Unit {
        return jpaMemberRepository.deleteById(member.id)
    }
}