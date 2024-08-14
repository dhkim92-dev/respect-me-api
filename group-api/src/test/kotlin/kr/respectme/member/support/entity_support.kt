package kr.respectme.member.support

import kr.respectme.member.domain.model.Member
import kr.respectme.member.domain.model.MemberRole
import kr.respectme.member.domain.model.JpaMemberEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime
import java.util.*

fun createJpaMembers(size: Int): List<JpaMemberEntity> {
    return List<JpaMemberEntity>(size) {
        createJpaMember(it)
    }
}

fun createJpaMember(idx: Int): JpaMemberEntity {
    return JpaMemberEntity(
        id = Member.randomUUID(),
        email = "member-${idx}@respect-me.kr",
        nickname = "member-${idx}",
        password = BCryptPasswordEncoder(10).encode("test1234"),
        isBlocked = false,
        blockReason = "",
        role = MemberRole.ROLE_MEMBER
    )
}

fun createMembers(size: Int): List<Member> {
    return List<Member>(size){
        createMember(it)
    }
}

fun createMember(idx: Int): Member {
    return Member(
        id = Member.randomUUID(),
        email = "member-${idx}@respect-me.kr",
        nickname = "member-${idx}",
        password = BCryptPasswordEncoder(10).encode("test1234"),
        isBlocked = false,
        blockReason = "",
        createdAt = LocalDateTime.now(),
        role = MemberRole.ROLE_MEMBER
    )
}