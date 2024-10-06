package kr.respectme.member.applications.adapter.command

import kr.respectme.common.error.BadRequestException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.member.applications.adapter.command.strategy.TokenValidationStrategyFactory
import kr.respectme.member.applications.dto.DeviceTokenDto
import kr.respectme.member.applications.dto.RegisterDeviceTokenCommand
import kr.respectme.member.applications.port.command.DeviceTokenCommandUseCase
import kr.respectme.member.common.code.MemberServiceErrorCode
import kr.respectme.member.domain.model.DeviceToken
import kr.respectme.member.domain.model.DeviceTokenType
import kr.respectme.member.infrastructures.persistence.port.command.MemberLoadPort
import kr.respectme.member.infrastructures.persistence.port.command.MemberSavePort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


/**
 * DeviceTokenService
 * DeviceTokenUseCase를 구현한 서비스 클래스
 */
@Service
class DeviceTokenService(
    private val memberLoadPort: MemberLoadPort,
    private val memberSavePort: MemberSavePort,
    private val deviceTokenValidationStrategyFactory: TokenValidationStrategyFactory
): DeviceTokenCommandUseCase {

    @Transactional
    override fun registerDeviceToken(loginId: UUID, memberId: UUID, command: RegisterDeviceTokenCommand): DeviceTokenDto {
        if(!validateDeviceToken(command.type, command.token)) {
            throw BadRequestException(MemberServiceErrorCode.INVALID_DEVICE_TOKEN)
        }

        if(loginId != memberId) {
            throw ForbiddenException(MemberServiceErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
        }

        val member = memberLoadPort.getMemberWithDeviceToken(memberId)
            ?: throw NotFoundException(MemberServiceErrorCode.MEMBER_NOT_FOUND)

        val token = member.deviceTokens
            .find { it.token == command.token}
            ?: DeviceToken(
                memberId = memberId,
                type = command.type,
                token = command.token
            )

        member.registerDeviceToken(token)
        val savedMember = memberSavePort.save(member)
        val savedDeviceToken = savedMember
            .deviceTokens
            .find{ it.id == token.id }
            ?: throw NotFoundException(MemberServiceErrorCode.DEVICE_TOKEN_NOT_FOUND)

        return DeviceTokenDto.valueOf(savedDeviceToken)
    }

    @Transactional
    override fun retrieveDeviceTokens(loginId: UUID, memberId: UUID): List<DeviceTokenDto> {
        if(loginId != memberId) {
            throw ForbiddenException(MemberServiceErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
        }

        val member = memberLoadPort.getMemberWithDeviceToken(memberId)
            ?: throw NotFoundException(MemberServiceErrorCode.MEMBER_NOT_FOUND)

        return member.deviceTokens.map {
            DeviceTokenDto.valueOf(it)
        }
    }

    @Transactional
    override fun deleteDeviceToken(loginId: UUID, memberId: UUID, tokenId: UUID) {
        if(loginId != memberId) {
            throw ForbiddenException(MemberServiceErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
        }

        val member = memberLoadPort.getMemberWithDeviceToken(memberId)
            ?: throw NotFoundException(MemberServiceErrorCode.MEMBER_NOT_FOUND)
        val token = member.deviceTokens.find { it.id == tokenId }
        val result = member.removeDeviceToken(token)

        memberSavePort.save(member)

        if(!result) {
            throw BadRequestException(MemberServiceErrorCode.FAILED_TO_DELETE_DEVICE_TOKEN)
        }
    }

    private fun validateDeviceToken(type: DeviceTokenType, token: String): Boolean {
        val strategy = deviceTokenValidationStrategyFactory.build(type)
        return strategy.validate(token)
    }
}