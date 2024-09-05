package kr.respectme.member.domain.mapper

import kr.respectme.member.domain.dto.MemberDto
import kr.respectme.member.domain.model.Member
import kr.respectme.member.infrastructures.persistence.jpa.JpaMemberEntity
import org.springframework.stereotype.Component

@Component
class MemberMapper {

    fun memberToMemberDto(member: Member) = MemberDto(
        id = member.id,
        nickname =  member.nickname,
        email = member.email,
        role = member.role,
        isBlocked = member.isBlocked,
        createdAt = member.createdAt,
        blockReason = member.blockReason
    )

    fun toDomainEntity(jpaMemberEntity: JpaMemberEntity) = Member(
        id = jpaMemberEntity.id,
        nickname = jpaMemberEntity.nickname,
        email = jpaMemberEntity.email,
        password = jpaMemberEntity.password,
        role = jpaMemberEntity.role,
        isBlocked = jpaMemberEntity.isBlocked,
        blockReason = jpaMemberEntity.blockReason,
        createdAt = jpaMemberEntity.createdAt
    )

    fun toJpaEntity(member: Member) = JpaMemberEntity(
        id = member.id,
        nickname = member.nickname,
        email = member.email,
        password = member.password,
        role = member.role,
        isBlocked = member.isBlocked,
        blockReason = member.blockReason,
    )
}