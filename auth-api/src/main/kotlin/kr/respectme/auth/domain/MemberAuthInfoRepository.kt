package kr.respectme.auth.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface MemberAuthInfoRepository: JpaRepository<MemberAuthInfo, MemberId> {

    fun findByOidcAuthPlatformAndOidcAuthUserIdentifier(platform: OidcPlatform, identifier: String): MemberAuthInfo?

    fun findByEmail(email: String): MemberAuthInfo?

    fun existsByOidcAuthPlatformAndOidcAuthUserIdentifier(platform: OidcPlatform, identifier: String): Boolean
}