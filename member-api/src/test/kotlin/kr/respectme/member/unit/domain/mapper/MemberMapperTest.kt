package kr.respectme.member.unit.domain.mapper

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.respectme.member.domain.dto.MemberDto
import kr.respectme.member.domain.mapper.MemberMapper
import kr.respectme.member.infrastructures.persistence.jpa.JpaMemberEntity
import kr.respectme.member.domain.model.Member
import kr.respectme.member.support.createJpaMember
import kr.respectme.member.support.createMember

internal class MemberMapperTest: AnnotationSpec() {

    private val memberMapper = MemberMapper()

    private fun checkEqual(domainEntity: Member, persistEntity: JpaMemberEntity) {
        persistEntity.id shouldBe domainEntity.id
        persistEntity.email shouldBe domainEntity.email
        persistEntity.nickname shouldBe domainEntity.nickname
        persistEntity.password shouldBe domainEntity.password
        persistEntity.isBlocked shouldBe domainEntity.isBlocked
        persistEntity.role shouldBe domainEntity.role
        persistEntity.blockReason shouldBe domainEntity.blockReason
    }

    private fun checkEqual(domainEntity: Member, dto: MemberDto) {
        domainEntity.id shouldBe dto.id
        domainEntity.email shouldBe dto.email
        domainEntity.nickname shouldBe dto.nickname
        domainEntity.role shouldBe dto.role
        domainEntity.isBlocked shouldBe dto.isBlocked
        domainEntity.blockReason shouldBe dto.blockReason
    }

    @Test
    fun `Convert Member to JpaMemberEntity`() {
        // Given
        val member = createMember(1)

        // When
        val entity = memberMapper.toJpaEntity(member)

        // Then
        checkEqual(member, entity)
    }

    @Test
    fun `Convert JpaMemberEntity to Member`() {
        // Given
        val jpaEntity = createJpaMember(1)

        // When
        val domainEntity = memberMapper.toDomainEntity(jpaEntity)

        // Then
        checkEqual(domainEntity, jpaEntity)
    }

    @Test
    fun `Convert Member to MemberDto`() {
        // Given
        val member = createMember(1)

        // When
        val dto = memberMapper.memberToMemberDto(member)

        //Then
        checkEqual(member, dto)
    }
}
