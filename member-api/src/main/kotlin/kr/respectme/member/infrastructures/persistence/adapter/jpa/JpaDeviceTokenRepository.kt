package kr.respectme.member.infrastructures.persistence.adapter.jpa

import kr.respectme.member.infrastructures.persistence.jpa.JpaDeviceTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaDeviceTokenRepository: org.springframework.data.repository.Repository<JpaDeviceTokenEntity, UUID> {

    fun findById(tokenId: UUID): JpaDeviceTokenEntity?
}