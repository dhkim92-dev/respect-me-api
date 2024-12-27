package kr.respectme.member.adapter.out.persistence.jpa

import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaDeviceTokenRepository: org.springframework.data.repository.Repository<JpaDeviceTokenEntity, UUID> {

    fun findById(tokenId: UUID): JpaDeviceTokenEntity?
}