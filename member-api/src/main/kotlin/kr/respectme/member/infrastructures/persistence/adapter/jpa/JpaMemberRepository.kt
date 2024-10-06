package kr.respectme.member.infrastructures.persistence.adapter.jpa

import kr.respectme.member.infrastructures.persistence.jpa.JpaMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaMemberRepository: JpaRepository<JpaMemberEntity, UUID> {

    fun findByEmail(email: String): JpaMemberEntity?

    @Query("SELECT m FROM member m LEFT JOIN FETCH m.deviceTokens WHERE m.id = :memberId")
    fun findByIdWithDeviceTokens(memberId: UUID): JpaMemberEntity?
}