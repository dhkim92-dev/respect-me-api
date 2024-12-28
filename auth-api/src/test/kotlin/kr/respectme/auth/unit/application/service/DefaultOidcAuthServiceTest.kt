package kr.respectme.auth.unit.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kr.respectme.auth.application.DefaultOidcAuthService
import kr.respectme.auth.application.dto.OidcMemberLoginCommand
import kr.respectme.auth.application.jwt.JwtService
import kr.respectme.auth.application.oidc.IdTokenVerifier
import kr.respectme.auth.application.useCase.AuthUseCase
import kr.respectme.auth.common.AuthenticationErrorCode
import kr.respectme.auth.domain.MemberAuthInfoRepository
import kr.respectme.auth.domain.OidcPlatform
import kr.respectme.auth.port.`in`.persistence.MemberLoadPort
import kr.respectme.auth.port.`in`.persistence.MemberSavePort
import kr.respectme.auth.support.*
import kr.respectme.common.error.UnauthorizedException
import kr.respectme.common.response.ApiResult

class DefaultOidcAuthServiceTest: BehaviorSpec({

    val jwtConfigs = createJwtConfigs(accessTokenExpiry = 600000, refreshTokenExpiry = 86400000L)
    val jwtService = JwtService(jwtConfigs)
    val memberAuthInfo = createMemberAuthInfo()
    val memberLoadPort = mockk<MemberLoadPort>()
    val memberSavePort = mockk<MemberSavePort>()
    val authInfoRepository = mockk<MemberAuthInfoRepository>()
    val authUseCase = mockk<AuthUseCase>()
    val idTokenVerifier = mockk<IdTokenVerifier>()

    val oidcAuthService = DefaultOidcAuthService(
        memberSavePort=memberSavePort,
        jwtService=jwtService,
        memberLoadPort=memberLoadPort,
        authInfoRepository=authInfoRepository,
        authUseCase = authUseCase,
        idTokenVerifier = idTokenVerifier
    )

    Given("유효한 OIDC Id Token이 주어진다") {
        val keyPair = createRSAKeyPair()
        val idToken = createSampleGoogleIdToken(
            keyPair = keyPair,
            sub = memberAuthInfo.memberId!!.id.toString(),
            email = memberAuthInfo.email,
        )

        When("회원 정보가 존재하지 않으면") {
            val member = createMemberEntityFromMemberService(
                id = memberAuthInfo.memberId!!.id,
                email = memberAuthInfo.email,
            )

            every { authInfoRepository.findByEmail(any()) } returns null
            every { memberSavePort.registerMember(any()) } returns ApiResult(data = member)
            every { authInfoRepository.save(any()) } returns memberAuthInfo
            every { memberLoadPort.loadMemberById(member.id) } returns ApiResult(data = member)
            every { idTokenVerifier.verifyToken(idToken) } returns createOidcCommonPayload(idToken)
            every { authInfoRepository.findByOidcAuthPlatformAndOidcAuthUserIdentifier(any(), any()) } returns null

            Then("회원 정보를 생성하고 토큰을 발급한다") {
                val request = OidcMemberLoginCommand(
                    platform = OidcPlatform.GOOGLE,
                    idToken = idToken,
                )

                val result = oidcAuthService.loginWithOidc(request)
                result.accessToken shouldNotBe null
                result.refreshToken shouldNotBe null
            }
        }

        When("회원 정보가 존재하면") {
            val member = createMemberEntityFromMemberService(
                id = memberAuthInfo.memberId!!.id,
                email = memberAuthInfo.email,
            )

            every { authInfoRepository.findByEmail(any()) } returns memberAuthInfo
            every { memberLoadPort.loadMemberById(member.id) } returns ApiResult(data = member)
            every { idTokenVerifier.verifyToken(idToken) } returns createOidcCommonPayload(idToken)
            every { authInfoRepository.findByOidcAuthPlatformAndOidcAuthUserIdentifier(any(), any()) } returns memberAuthInfo

            Then("토큰을 발급한다") {
                val request = OidcMemberLoginCommand(
                    platform = OidcPlatform.GOOGLE,
                    idToken = idToken
                )

                val result = oidcAuthService.loginWithOidc(request)
                result.accessToken shouldNotBe null
                result.refreshToken shouldNotBe null
            }
        }

        When("Id Token이 유효하지 않으면") {
            every { idTokenVerifier.verifyToken(idToken) } throws UnauthorizedException(AuthenticationErrorCode.OIDC_ID_TOKEN_VERIFICATION_FAILED)

            Then("예외를 발생시킨다") {
                shouldThrow<UnauthorizedException> {
                    oidcAuthService.loginWithOidc(OidcMemberLoginCommand(
                        platform = OidcPlatform.GOOGLE,
                        idToken = idToken
                    ))
                }
            }
        }
    }

    afterEach {
        clearMocks(memberLoadPort, memberSavePort, authInfoRepository, authUseCase)
    }

    afterContainer {
        clearAllMocks()
    }
})