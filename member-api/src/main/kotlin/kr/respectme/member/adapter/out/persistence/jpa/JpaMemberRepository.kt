package kr.respectme.member.adapter.out.persistence.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaMemberRepository: JpaRepository<JpaMemberEntity, UUID> {

    fun findByEmail(email: String): JpaMemberEntity?

    @Query("SELECT m FROM member m LEFT JOIN FETCH m.deviceTokens WHERE m.id = :memberId")
    fun findByIdWithDeviceTokens(memberId: UUID): JpaMemberEntity?

    @Query("""
        SELECT m 
        FROM member m 
        JOIN FETCH m.deviceTokens
        WHERE m.id IN :memberIds
    """)
    fun findByIds(memberIds: List<UUID>): List<JpaMemberEntity>
}