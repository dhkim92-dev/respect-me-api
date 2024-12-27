package kr.respectme.member.domain.mapper

import kr.respectme.member.domain.model.DeviceToken
import kr.respectme.member.adapter.out.persistence.jpa.JpaDeviceTokenRepository
import kr.respectme.member.adapter.out.persistence.jpa.JpaDeviceTokenEntity
import kr.respectme.member.adapter.out.persistence.jpa.JpaMemberEntity
import org.springframework.stereotype.Component

@Component
class DeviceTokenMapper(
    private val jpaDeviceTokenRepository: JpaDeviceTokenRepository
){

    fun toDomainEntity(jpaEntity: JpaDeviceTokenEntity): DeviceToken {
        return DeviceToken(
            id = jpaEntity.id,
            memberId = jpaEntity.member.id,
            token = jpaEntity.token,
            type = jpaEntity.type,
            createdAt = jpaEntity.createdAt,
            lastUsedAt = jpaEntity.lastUsedAt,
            isActivated = jpaEntity.isActivated,
            isDeleted = jpaEntity.isDeleted
        )
    }

    fun toJpaEntity(member: JpaMemberEntity, domainEntity: DeviceToken): JpaDeviceTokenEntity {
        val jpa = jpaDeviceTokenRepository.findById(domainEntity.id)
            ?.apply{
                token = domainEntity.getToken()
                type = domainEntity.getType()
                lastUsedAt = domainEntity.getLastUsedAt()
                isActivated = domainEntity.getIsActivated()
                isDeleted = domainEntity.getIsDeleted()
            } ?: mapToJpaEntity(member, domainEntity)
        return jpa
    }

    private fun mapToJpaEntity(member: JpaMemberEntity, domainEntity: DeviceToken): JpaDeviceTokenEntity {
        return JpaDeviceTokenEntity(
            id = domainEntity.id,
            member = member,
            token = domainEntity.getToken(),
            type = domainEntity.getType(),
            lastUsedAt = domainEntity.getLastUsedAt(),
            isActivated = domainEntity.getIsActivated(),
            isDeleted = domainEntity.getIsDeleted()
        )
    }
}