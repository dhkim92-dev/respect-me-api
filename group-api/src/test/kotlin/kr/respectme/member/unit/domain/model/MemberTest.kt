package kr.respectme.member.unit.domain.model

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.respectme.member.support.createMember
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

internal class MemberTest: AnnotationSpec() {

    @Test
    fun `nickname change`() {
        // Given
        val member = createMember(1)
        val newNickname = "newNickname"

        // When
        member.changeNickname(newNickname)

        // Then
        member.nickname shouldBe newNickname
    }

    @Test
    fun `password change`() {
        // Given
        val member = createMember(1)
        val newPassword = "newPassword"
        val passwordEncoder = BCryptPasswordEncoder(10)

        // When
        member.changePassword(passwordEncoder, newPassword)

        // Then
        passwordEncoder.matches(newPassword, member.password) shouldBe true
    }

    @Test
    fun `block member`() {
        // Given
        val member = createMember(1)
        val reason = "Suspicious action"

        // When
        member.block(reason)

        // Then
        member.isBlocked shouldBe true
        member.blockReason shouldBe reason
    }

    @Test
    fun `unblock member`() {
        // Given
        val member = createMember(1)
        val reason = "Suspicious action"
        member.block(reason)

        // When

        member.unblock()

        // Then
        member.isBlocked shouldBe false
        member.blockReason shouldBe ""
    }
}