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
import kr.respectme.auth.infrastructures.dto.CreateMemberRequest
import kr.respectme.auth.infrastructures.dto.Member
import kr.respectme.auth.infrastructures.ports.MemberLoadPort
import kr.respectme.auth.infrastructures.ports.MemberSavePort
import kr.respectme.common.error.BadRequestException
import kr.respectme.common.error.UnauthorizedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    @Transactional
    override fun loginWithOidc(command: OidcMemberLoginCommand): AuthenticationResult {
        val payload = idTokenVerifier.verifyToken(command.idToken)
        var memberAuthInfo = authInfoRepository.findByOidcAuthPlatformAndOidcAuthUserIdentifier(command.platform, payload.sub)

        if(memberAuthInfo == null) {
            val member = processMemberRegistration(command, payload)
            memberAuthInfo = authInfoRepository.save(MemberAuthInfo(
                memberId = MemberId.of(member.id),
                email = member.email,
                oidcAuth = OidcAuth(
                    platform = command.platform,
                    userIdentifier = payload.sub
                )
            ))
        }

        var memberInfo = memberLoadPort.loadMemberById(memberAuthInfo.memberId?.id!!).data
            ?: throw UnauthorizedException(AuthenticationErrorCode.FAILED_TO_SIGN_IN)
        val refreshToken = jwtService.createRefreshToken(memberInfo.id)
        val accessToken = jwtService.createAccessToken(memberInfo)

        return AuthenticationResult(
            type = "Bearer",
            memberId = memberInfo.id,
            refreshToken = refreshToken,
            accessToken = accessToken,
        )
    }

    private fun processMemberRegistration(command: OidcMemberLoginCommand, payload: CommonOidcIdTokenPayload): Member {
        val member = memberSavePort.registerMember(createMemberRequest(command, payload))
            .data
        return member
    }

    private fun createMemberRequest(command: OidcMemberLoginCommand, payload: CommonOidcIdTokenPayload): CreateMemberRequest {
        return CreateMemberRequest(
            email = payload.email,
            nickname = command.nickname ?: throw BadRequestException(AuthenticationErrorCode.REQUIRE_NICKNAME_TO_JOIN_SERVICE_BY_OIDC),
            profileImageUrl = payload.profileImageUrl
        )
    }
}