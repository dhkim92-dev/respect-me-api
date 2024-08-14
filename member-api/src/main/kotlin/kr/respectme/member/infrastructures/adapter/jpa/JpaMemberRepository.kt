package kr.respectme.member.infrastructures.adapter.jpa

import kr.respectme.member.domain.model.JpaMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaMemberRepository: JpaRepository<JpaMemberEntity, UUID> {

    fun findByEmail(email: String): JpaMemberEntity?
}