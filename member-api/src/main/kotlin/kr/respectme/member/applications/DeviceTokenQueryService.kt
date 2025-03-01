package kr.respectme.member.applications

import kr.respectme.common.error.ForbiddenException
import kr.respectme.member.applications.dto.DeviceTokenDto
import kr.respectme.member.applications.usecase.query.DeviceTokenQueryUseCase
import kr.respectme.member.common.code.MemberServiceErrorCode
import kr.respectme.member.port.out.persistence.query.DeviceTokenQueryRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DeviceTokenQueryService(
    private val deviceTokenQueryRepository: DeviceTokenQueryRepository
): DeviceTokenQueryUseCase {

    override fun retrieveDeviceTokens(loginId: UUID, memberId: UUID): List<DeviceTokenDto> {
        if(loginId != memberId) {
            throw ForbiddenException(MemberServiceErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
        }

        return deviceTokenQueryRepository.findAllByMemberId(loginId)
            .map { DeviceTokenDto.valueOf(it) }
    }

    override fun retrieveDeviceToken(loginId: UUID, memberId: UUID, tokenId: UUID): DeviceTokenDto {
        return deviceTokenQueryRepository.findById(tokenId)
            ?.let {
                if(it.member.id != loginId || memberId != it.member.id) {
                    throw ForbiddenException(MemberServiceErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
                }
                DeviceTokenDto.valueOf(it)
            }
            ?: throw ForbiddenException(MemberServiceErrorCode.DEVICE_TOKEN_NOT_FOUND)
    }
}