package kr.respectme.auth.unit.domain

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.respectme.auth.domain.MemberAuthInfo
import kr.respectme.auth.domain.MemberId
import kr.respectme.auth.domain.OidcAuth
import java.time.Instant
import java.util.*

class MemberAuthInfoTest : AnnotationSpec() {

    @Test
    fun `MemberAuthInfo constructor test`() {
        // Given
        // 생성자 정보가 주어지고
        val id = MemberId(UUID.randomUUID())
        val email = "test-email"
        val password = "test-password"
        val oidcAuth = OidcAuth()
        val lastLoginAt = Instant.now()

        // When
        // MemberAuthInfo를 생성하면
        val member = MemberAuthInfo(
            memberId = id,
            email = email,
            password = password,
            oidcAuth = oidcAuth,
            lastLoginAt = lastLoginAt
        )

        // Then
        // 객체가 정상적으로 생성된다.
        member.getMemberId() shouldBe id
        member.getEmail() shouldBe email
        member.getPassword() shouldBe password
        member.getOidcAuth() shouldBe oidcAuth
        member.getLastLoginAt() shouldBe lastLoginAt
    }

    @Test
    fun `equals method test`() {
        // Given
        // 서로 다른 ID를 가진 MemberAuthInfo가 주어지고
        val id1 = MemberId(UUID.randomUUID())
        val id2 = MemberId(UUID.randomUUID())
        val email = "test-email"
        val password = "test-password"
        val oidcAuth = OidcAuth()
        val lastLoginAt = Instant.now()

        val member1 = MemberAuthInfo(
            memberId = id1,
            email = email,
            password = password,
            oidcAuth = oidcAuth,
            lastLoginAt = lastLoginAt
        )
        val member2 = MemberAuthInfo(
            memberId = id2,
            email = email,
            password = password,
            oidcAuth = oidcAuth,
            lastLoginAt = lastLoginAt
        )

        // When
        // 두 객체를 비교하면
        val result = member1 == member2

        // Then
        // 다르다고 나온다.
        result shouldBe false
    }

    @Test
    fun `hashCode Method test`() {
        // Given
        // 서로 다른 ID를 가진 MemberAuthInfo가 주어지고
        val id1 = MemberId(UUID.randomUUID())
        val id2 = MemberId(UUID.randomUUID())
        val email = "test-email"
        val password = "test-password"
        val oidcAuth = OidcAuth()
        val lastLoginAt = Instant.now()

        // When
        // 두 객체의 hashCode를 비교하면
        val result = MemberAuthInfo(
            memberId = id1,
            email = email,
            password = password,
            oidcAuth = oidcAuth,
            lastLoginAt = lastLoginAt
        ).hashCode() == MemberAuthInfo(
            memberId = id2,
            email = email,
            password = password,
            oidcAuth = oidcAuth,
            lastLoginAt = lastLoginAt).hashCode()

        // Then
        // 다르다고 나온다.
        result shouldBe false
    }
}