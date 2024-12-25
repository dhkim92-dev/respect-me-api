package kr.respectme.auth.unit.domain

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.respectme.auth.domain.MemberAuthInfo
import kr.respectme.auth.domain.MemberAuthInfoRepository
import kr.respectme.auth.domain.OidcPlatform
import kr.respectme.auth.support.createMemberAuthInfo
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class MemberAuthInfoRepositoryTest(
    private val memberAuthInfoRepository: MemberAuthInfoRepository
): AnnotationSpec() {

    private val memberInfos: MutableList<MemberAuthInfo> = mutableListOf()

    @BeforeEach
    fun setUp() {
        memberInfos.clear()
        repeat(10) {
            memberInfos.add(createMemberAuthInfo())
        }
        memberAuthInfoRepository.saveAll(memberInfos)
    }

    @Test
    fun `findByEmail Success Test`() {
        // Given
        val email = memberInfos.get(0).email;

        // When
        val member = memberAuthInfoRepository.findByEmail(email)

        //Then
        member shouldBe memberInfos.get(0)
    }

    @Test
    fun `findByEmail Fail Test`() {
        // Given
        val email = "unknown-email@respect-me.kr"

        // When
        val member = memberAuthInfoRepository.findByEmail(email)

        //Then
        member shouldBe null
    }

    @Test
    fun `findByOidcAuthPlatformAndOidcAuthUserIdentifier Success Test`() {
        // Given
        val member = memberInfos.get(0)
        val platform = member.oidcAuth.platform
        val identifier = member.oidcAuth.userIdentifier

        // When
        val result = memberAuthInfoRepository.findByOidcAuthPlatformAndOidcAuthUserIdentifier(platform, identifier!!)

        // Then
        result shouldBe member
    }

    @Test
    fun `findByOidcAuthPlatformAndOidcAuthUserIdentifier Fail Test`() {
        // Given
        // Given
        val platform = OidcPlatform.APPLE
        val identifier = "doesnt-exist-identifier"

        // When
        val result = memberAuthInfoRepository.findByOidcAuthPlatformAndOidcAuthUserIdentifier(platform, identifier!!)

        // Then
        result shouldBe null
    }

    @Test
    fun `existsByOidcAuthPlatformAndOidcAuthUserIdentifier Success Test`() {
        // Given
        val member = memberInfos.get(0)
        val platform = member.oidcAuth.platform
        val identifier = member.oidcAuth.userIdentifier

        // When
        val result = memberAuthInfoRepository.existsByOidcAuthPlatformAndOidcAuthUserIdentifier(platform, identifier!!)

        // Then
        result shouldBe true
    }

    @Test
    fun `existsByOidcAuthPlatformAndOidcAuthUserIdentifier Fail Test`() {
        // Given
        val platform = OidcPlatform.APPLE
        val identifier = "doesnt-exist-identifier"

        // When
        val result = memberAuthInfoRepository.existsByOidcAuthPlatformAndOidcAuthUserIdentifier(platform, identifier!!)

        // Then
        result shouldBe false
    }

    @AfterEach
    fun tearDown() {
        memberAuthInfoRepository.deleteAll()
    }
}