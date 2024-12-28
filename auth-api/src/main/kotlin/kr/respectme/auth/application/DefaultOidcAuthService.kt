package kr.respectme.auth.application

import kr.respectme.auth.application.dto.AuthenticationResult
import kr.respectme.auth.application.dto.OidcMemberLoginCommand
import kr.respectme.auth.application.useCase.AuthUseCase
import kr.respectme.auth.application.oidc.idToken.CommonOidcIdTokenPayload
import kr.respectme.auth.application.useCase.OidcAuthUseCase
import kr.respectme.auth.common.AuthenticationErrorCode
import kr.respectme.auth.application.jwt.JwtService
import kr.respectme.auth.application.oidc.IdTokenVerifier
import kr.respectme.auth.domain.*
import kr.respectme.auth.port.`in`.msa.members.dto.CreateMemberRequest
import kr.respectme.auth.port.`in`.msa.members.dto.Member
import kr.respectme.auth.port.`in`.persistence.MemberLoadPort
import kr.respectme.auth.port.`in`.persistence.MemberSavePort
import kr.respectme.common.error.InternalServerError
import kr.respectme.common.error.UnauthorizedException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * OIDC 인증을 처리하는 서비스
 * @property authInfoRepository MemberAuthInfoRepository
 * @property memberSavePort MemberSavePort
 * @property memberLoadPort MemberLoadPort
 * @property idTokenVerifier IdTokenVerifier
 * @property authUseCase AuthUseCase
 * @property jwtService JwtService
 */
@Service
class DefaultOidcAuthService(
    private val authInfoRepository: MemberAuthInfoRepository,
    private val memberSavePort: MemberSavePort,
    private val memberLoadPort: MemberLoadPort,
    private val idTokenVerifier: IdTokenVerifier,
    private val authUseCase: AuthUseCase,
    private val jwtService: JwtService
): OidcAuthUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun loginWithOidc(command: OidcMemberLoginCommand): AuthenticationResult {
        val payload = idTokenVerifier.verifyToken(command.idToken)
        var memberAuthInfo = authInfoRepository.findByOidcAuthPlatformAndOidcAuthUserIdentifier(command.platform, payload.sub)

        if(memberAuthInfo == null) {
            val member = processMemberRegistration(command, payload)
            memberAuthInfo = createMemberAuthInfo(member.id, command.platform, payload)
        }

        val memberInfo = memberLoadPort.loadMemberById(memberAuthInfo.memberId?.id!!).data

        if(memberInfo == null) {
            processMemberRegistration(command, payload)
        }

        memberInfo as Member

        val refreshToken = jwtService.createRefreshToken(memberInfo.id)
        val accessToken = jwtService.createAccessToken(memberInfo)

        return AuthenticationResult(
            type = "Bearer",
            memberId = memberInfo.id,
            refreshToken = refreshToken,
            accessToken = accessToken,
        )
    }

    private fun createMemberAuthInfo(memberId: UUID, platform: OidcPlatform, payload: CommonOidcIdTokenPayload): MemberAuthInfo {
        val memberAuthInfo = MemberAuthInfo(
            memberId = MemberId.of(memberId),
            email = payload.email,
            oidcAuth = OidcAuth(
                platform = platform,
                userIdentifier = payload.sub
            )
        )

        return try {
            authInfoRepository.save(memberAuthInfo)
        } catch(e: Exception) {
            rollbackMemberRegistration(memberId)
            throw UnauthorizedException(AuthenticationErrorCode.FAILED_TO_SIGN_IN)
        }
    }

    private fun processMemberRegistration(command: OidcMemberLoginCommand, payload: CommonOidcIdTokenPayload): Member {
        val member = memberSavePort.registerMember(createMemberRequest(command, payload))
            .data
        return member
    }

    private fun rollbackMemberRegistration(memberId: UUID) {
        logger.info("Oidc member join request failed, rollback transaction memberId: ${memberId}")

        try {
            memberSavePort.deleteMember(memberId)
        } catch(e: Exception) {
            logger.error("[ACID CRITICAL] Failed to rollback member registration member: ${memberId} with message : ${e.message}")
            throw InternalServerError(message = "Oidc Member join request failed, and failed to rollback transaction.")
        }
    }

    private fun createMemberRequest(command: OidcMemberLoginCommand, payload: CommonOidcIdTokenPayload): CreateMemberRequest {
        return CreateMemberRequest(
            email = payload.email,
            profileImageUrl = payload.profileImageUrl
        )
    }
}