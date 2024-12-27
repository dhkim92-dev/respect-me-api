package kr.respectme.member.domain.mapper

import kr.respectme.member.domain.dto.MemberDto
import kr.respectme.member.domain.model.Member
import kr.respectme.member.adapter.out.persistence.jpa.JpaMemberRepository
import kr.respectme.member.adapter.out.persistence.jpa.JpaMemberEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class MemberMapper(
    private val jpaMemberRepository: JpaMemberRepository,
    private val deviceTokenMapper: DeviceTokenMapper
) {

    fun memberToMemberDto(member: Member) = MemberDto(
        id = member.id,
        email = member.getEmail(),
        role = member.getRole(),
        isBlocked = member.getIsBlocked(),
        createdAt = member.createdAt,
        blockReason = member.getBlockReason(),
        deviceTokens = member.getDeviceTokens()
            .map { it->it.getToken() }
            .toList()
    )

    fun toDomainEntity(jpaMemberEntity: JpaMemberEntity) = Member(
        id = jpaMemberEntity.id,
        email = jpaMemberEntity.email,
        role = jpaMemberEntity.role,
        isBlocked = jpaMemberEntity.isBlocked,
        blockReason = jpaMemberEntity.blockReason,
        createdAt = jpaMemberEntity.createdAt,
        deviceTokens = jpaMemberEntity.deviceTokens.map { deviceTokenMapper.toDomainEntity(it) }.toMutableSet(),
        isDeleted = jpaMemberEntity.isDeleted
    )

    fun toJpaEntity(member: Member): JpaMemberEntity {
        val jpaEntity = jpaMemberRepository.findByIdOrNull(member.id)
            ?: JpaMemberEntity(id = member.id,)
        jpaEntity.apply {
                this.email = member.getEmail()
                this.role = member.getRole()
                this.isBlocked = member.getIsBlocked()
                this.blockReason = member.getBlockReason()
                this.isDeleted = member.getIsDeleted()
                member.getDeviceTokens().forEach {
                    this.deviceTokens.add(deviceTokenMapper.toJpaEntity(this, it))
                }
            }
        return jpaEntity
    }
}