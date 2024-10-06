package kr.respectme.member.infrastructures.persistence.port.query

import kr.respectme.member.infrastructures.persistence.jpa.JpaDeviceTokenEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DeviceTokenQueryRepository: org.springframework.data.repository.Repository<JpaDeviceTokenEntity, UUID> {

    @Query("""
        SELECT d
        FROM device_token d
        JOIN FETCH d.member
        WHERE d.id = :id
    """)
    fun findById(id: UUID): JpaDeviceTokenEntity?

    @Query("""SELECT d FROM device_token d JOIN FETCH d.member WHERE d.member.id = :memberId""")
    fun findAllByMemberId(memberId: UUID): List<JpaDeviceTokenEntity>
}