package kr.respectme.auth.unit.application.service

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.respectme.auth.application.AuthService
import kr.respectme.auth.application.dto.AuthenticationResult
import kr.respectme.auth.application.dto.JwtAccessTokenVerifierRequiredInfo
import kr.respectme.auth.application.jwt.JwtService
import kr.respectme.auth.domain.MemberAuthInfoRepository
import kr.respectme.auth.port.`in`.interfaces.dto.LoginRequest
import kr.respectme.auth.port.out.persistence.member.dto.Member
import kr.respectme.auth.port.out.persistence.member.MemberLoadPort
import kr.respectme.auth.support.*
import kr.respectme.common.error.NotFoundException
import kr.respectme.common.error.UnauthorizedException
import kr.respectme.common.response.ApiResult
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class AuthServiceTest() : BehaviorSpec({

    val jwtConfigs = createJwtConfigs(accessTokenExpiry = 600000, refreshTokenExpiry = 86400000L)
    val jwtService = JwtService(jwtConfigs)
    val memberLoadPort = mockk<MemberLoadPort>()
    val authInfoRepository = mockk<MemberAuthInfoRepository>()
    val memberAuthInfo = createMemberAuthInfo()
    val passwordEncoder = BCryptPasswordEncoder()
    val existMember = createMemberAuthInfo()
    val authService = AuthService(jwtService, jwtConfigs, memberLoadPort, authInfoRepository, passwordEncoder)

    Given("로그인 요청이 주어진다.") {
        val request = LoginRequest(email = existMember.email, password = "test1234")
        val invalidPasswordRequest = LoginRequest(email = existMember.email, password = "invalidPassword")

        When("회원 정보가 존재하지 않으면") {
            every { authInfoRepository.findByEmail(any()) } returns null
            Then("로그인에 실패한다") {
                val exception = shouldThrowAny {
                    authService.login(request)
                }

                exception::class shouldBe NotFoundException::class
            }
        }

        When("회원 정보가 존재하지만 비밀번호가 일치하지 않으면") {
            every { authInfoRepository.findByEmail(any()) } returns memberAuthInfo

            Then("로그인에 실패한다") {
                val exception = shouldThrowAny {
                    authService.login(invalidPasswordRequest)
                }

                exception::class shouldBe UnauthorizedException::class
            }
        }

        When("회원 정보가 존재하고 비밀번호가 일치하면") {
            val response = ApiResult<Member?>(data=createMemberEntityFromMemberService())
            every { authInfoRepository.findByEmail(any()) } returns memberAuthInfo
            every { memberLoadPort.loadMemberById(any()) } returns response
            every { authInfoRepository.save(any()) } returns memberAuthInfo

            Then("로그인에 성공한다") {
                val result = authService.login(request)

                result::class shouldBe AuthenticationResult::class
                result.accessToken shouldNotBe null
                result.refreshToken shouldNotBe null
                result.memberId shouldNotBe existMember.memberId?.id
            }
        }
    }

    Given("엑세스 토큰 갱신 요청이 들어온다") {
        val validRefreshToken = jwtService.createRefreshToken(memberAuthInfo.memberId!!.id)
        val invalidAccessToken = jwtService.createAccessToken(
            Member(
            id = memberAuthInfo.memberId!!.id,
            email = memberAuthInfo.email,
            role = "ROLE_ADMIN",
            isBlocked = false,
            blockReason = ""
        )
        )

        When("리프레시 토큰이 유효하지 않으면") {
            val exception = shouldThrowAny {
                authService.refreshAccessToken(invalidAccessToken)
            }
            Then("토큰 갱신에 실패한다") {
                exception::class shouldBe UnauthorizedException::class
            }
        }

        When("리프레시 토큰이 유효하면") {
            val result = authService.refreshAccessToken(validRefreshToken)
            Then("토큰 갱신에 성공한다") {
                result::class shouldBe AuthenticationResult::class
                result.accessToken shouldNotBe null
                result.refreshToken shouldNotBe null
                result.memberId shouldNotBe memberAuthInfo.memberId?.id
            }
        }
    }

    Given("JWT Token 검증을 위한 정보를 요청한다") {
        val validServiceAccountToken = createServiceAccountToken(jwtConfigs)
        val invalidToken = createAccessToken(createMemberEntityFromMemberService(), jwtConfigs)
        When("요청자의 JWT Token의 파싱 결과 내부 서비스가 아니라면") {
            val exception = shouldThrowAny {
                authService.retrieveAccessTokenVerifierRequiredInfo(invalidToken)
            }

            Then("에러가 발생한다") {
                exception::class shouldBe UnauthorizedException::class
            }
        }

        When("내부 서비스라면") {
            val result = authService.retrieveAccessTokenVerifierRequiredInfo("Bearer " +      validServiceAccountToken)

            Then("토큰 검증 정보를 반환한다") {
                result::class shouldBe JwtAccessTokenVerifierRequiredInfo::class
            }
        }
    }
})