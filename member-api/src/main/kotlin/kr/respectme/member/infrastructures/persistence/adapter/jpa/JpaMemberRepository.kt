package kr.respectme.member.infrastructures.persistence.adapter.jpa

import kr.respectme.member.infrastructures.persistence.jpa.JpaMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaMemberRepository: JpaRepository<JpaMemberEntity, UUID> {

    fun findByEmail(email: String): JpaMemberEntity?
}