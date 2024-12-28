package kr.respectme.auth.application

import com.auth0.jwt.exceptions.TokenExpiredException
import kr.respectme.auth.application.dto.AuthenticationResult
import kr.respectme.auth.application.dto.JwtAccessTokenVerifierRequiredInfo
import kr.respectme.auth.application.useCase.AuthUseCase
import kr.respectme.auth.common.AuthenticationErrorCode
import kr.respectme.auth.application.jwt.JwtService
import kr.respectme.auth.configs.JwtConfigs
import kr.respectme.auth.domain.MemberAuthInfoRepository
import kr.respectme.auth.port.`in`.interfaces.dto.LoginRequest
import kr.respectme.auth.port.`in`.persistence.MemberLoadPort
import kr.respectme.common.error.NotFoundException
import kr.respectme.common.error.UnauthorizedException
import kr.respectme.common.security.jwt.JwtClaims
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AuthService(
    private val jwtService: JwtService,
    private val jwtConfigs: JwtConfigs,
    private val memberLoadPort: MemberLoadPort,
    private val authInfoRepository: MemberAuthInfoRepository,
    private val passwordEncoder: PasswordEncoder
): AuthUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val jwtVerificationRequirements = JwtAccessTokenVerifierRequiredInfo(
        issuer = jwtConfigs.issuer,
        secret = jwtConfigs.accessTokenSecretKey
    )

    @Transactional
    override fun login(loginRequest: LoginRequest): AuthenticationResult {
        logger.debug("login request: ${loginRequest}")
        val memberAuthInfo = authInfoRepository.findByEmail(loginRequest.email)
            ?: throw NotFoundException(AuthenticationErrorCode.FAILED_TO_SIGN_IN)

        if(!passwordEncoder.matches(loginRequest.password, memberAuthInfo.password)) {
            throw UnauthorizedException(AuthenticationErrorCode.FAILED_TO_SIGN_IN)
        }

        val member = memberLoadPort.loadMemberById(memberAuthInfo.memberId?.id!!).data
            ?: throw NotFoundException(AuthenticationErrorCode.FAILED_TO_SIGN_IN)

        val refreshToken = jwtService.createRefreshToken(member.id)
        val accessToken = jwtService.createAccessToken(member)

        memberAuthInfo.login()
        authInfoRepository.save(memberAuthInfo)

        return AuthenticationResult(
            type = "Bearer",
            memberId = member.id,
            refreshToken = refreshToken,
            accessToken = accessToken,
        )
    }

    override fun retrieveAccessTokenVerifierRequiredInfo(accessToken: String): JwtAccessTokenVerifierRequiredInfo {
        logger.debug("token: ${accessToken}")
        if(!accessToken.startsWith("Bearer ")) {
            logger.debug("token is not started with Bearer")
            throw UnauthorizedException(AuthenticationErrorCode.INVALID_ACCESS_TOKEN)
        }


        val extractedToken = accessToken.substring(7)
        val decoded = jwtService.verifyAccessToken(extractedToken)
        logger.debug("decoded : ${decoded.claims}")
        val payload = JwtClaims.valueOf(decoded)
        logger.debug("payload : ${payload}")

        if(!payload.roles.contains("ROLE_SERVICE")) {
            throw UnauthorizedException(AuthenticationErrorCode.ONLY_SERVICE_CAN_ACCESS)
        }

        return jwtVerificationRequirements
    }

    override fun refreshAccessToken(refreshToken: String): AuthenticationResult {
        try {
            val decoded = jwtService.verifyRefreshToken(refreshToken)
            val member = memberLoadPort.loadMemberById(UUID.fromString(decoded.subject)).data
                ?: throw NotFoundException(AuthenticationErrorCode.INVALID_REFRESH_TOKEN)
            return AuthenticationResult(
                type = "Bearer",
                memberId = member.id,
                refreshToken = refreshToken,
                accessToken = jwtService.createAccessToken(member)
            )
        } catch (e: Exception) {
            when (e) {
                is TokenExpiredException -> throw UnauthorizedException(AuthenticationErrorCode.EXPIRED_REFRESH_TOKEN)
                else -> throw UnauthorizedException(AuthenticationErrorCode.INVALID_REFRESH_TOKEN)
            }
        }
    }


}