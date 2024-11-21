package kr.respectme.member.domain.mapper

import kr.respectme.member.domain.dto.MemberDto
import kr.respectme.member.domain.model.Member
import kr.respectme.member.infrastructures.persistence.adapter.jpa.JpaMemberRepository
import kr.respectme.member.infrastructures.persistence.jpa.JpaMemberEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class MemberMapper(
    private val jpaMemberRepository: JpaMemberRepository,
    private val deviceTokenMapper: DeviceTokenMapper
) {

    fun memberToMemberDto(member: Member) = MemberDto(
        id = member.id,
        nickname =  member.nickname,
        email = member.email,
        role = member.role,
        isBlocked = member.isBlocked,
        createdAt = member.createdAt,
        blockReason = member.blockReason,
        deviceTokens = member.deviceTokens
            .map { it->it.token }
            .toList()
    )

    fun toDomainEntity(jpaMemberEntity: JpaMemberEntity) = Member(
        id = jpaMemberEntity.id,
        nickname = jpaMemberEntity.nickname,
        email = jpaMemberEntity.email,
        password = jpaMemberEntity.password,
        role = jpaMemberEntity.role,
        isBlocked = jpaMemberEntity.isBlocked,
        blockReason = jpaMemberEntity.blockReason,
        createdAt = jpaMemberEntity.createdAt,
        deviceTokens = jpaMemberEntity.deviceTokens.map { deviceTokenMapper.toDomainEntity(it) }.toMutableSet()
    )

    fun toJpaEntity(member: Member): JpaMemberEntity {
        println("member mapper to jpa entity")
        println("member mapper member tokens size : ${member.deviceTokens.size}")
        val jpaEntity = jpaMemberRepository.findByIdOrNull(member.id)
            ?: JpaMemberEntity(id = member.id,)
        jpaEntity.apply {
                println("member mapper to jpa entity apply")
                this.nickname = member.nickname
                this.password = member.password
                this.email = member.email
                this.role = member.role
                this.isBlocked = member.isBlocked
                this.blockReason = member.blockReason
                member.deviceTokens.forEach {
                    this.deviceTokens.add(deviceTokenMapper.toJpaEntity(this, it))
                }
            }
        return jpaEntity
    }
}