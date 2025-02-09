package kr.respectme.report.application.dto

import kr.respectme.report.application.vo.ReportDetailVo
import kr.respectme.report.domain.ReportEntity
import kr.respectme.report.domain.ReportStatus
import kr.respectme.report.domain.ResourceType
import java.util.UUID

data class ReportCommandModelDto(
    val id: Long,
    val reportMemberId: UUID,
    val status: ReportStatus,
    val resourceType: ResourceType,
    val resourceId: UUID,
    val content: String
) {

    companion object {

        fun valueOf(entity: ReportEntity): ReportCommandModelDto {
            return ReportCommandModelDto(
                id = entity.identifier,
                reportMemberId = entity.reportMemberId,
                status = entity.status,
                resourceType = entity.resourceType,
                resourceId = entity.resourceId,
                content = entity.reportDetails.firstOrNull()?.content ?: ""
            )
        }
    }
}