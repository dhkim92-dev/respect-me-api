package kr.respectme.auth.support

import kr.respectme.auth.domain.MemberAuthInfo
import kr.respectme.auth.domain.MemberId
import kr.respectme.auth.domain.OidcAuth
import kr.respectme.auth.domain.OidcPlatform
import kr.respectme.auth.port.`in`.msa.members.dto.Member
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*
import kotlin.random.Random


fun createMemberAuthInfo(): MemberAuthInfo {
    val password = BCryptPasswordEncoder().encode("test1234")

    return MemberAuthInfo(
        memberId = MemberId.of(UUID.randomUUID()),
        email = "${UUID.randomUUID()}@respect-me.kr",
        password = password,
        oidcAuth = createOidcInfo(),
    )
}

fun createMemberEntityFromMemberService(
    id: UUID = UUID.randomUUID(),
    email: String = "${id}@respect-me.kr",
    nickname: String = "test",
    role: String = "ROLE_USER",
    isBlocked: Boolean = false,
    blockReason: String = ""
): Member {
    return Member(
        id = id,
        email = email,
        role = role,
        isBlocked = isBlocked,
        blockReason = blockReason
    )
}

private fun createOidcInfo(): OidcAuth {
    val value = Random(1).nextInt() % 2 + 1;

    return when(value) {
        1 -> OidcAuth(
            platform = OidcPlatform.GOOGLE,
            userIdentifier = UUID.randomUUID().toString()
        )
        else -> OidcAuth(
            platform = OidcPlatform.APPLE,
            userIdentifier = UUID.randomUUID().toString()
        )
    }
}