package kr.respectme.report.port.`in`.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.report.application.dto.ReportCommandModelDto
import kr.respectme.report.domain.ReportStatus
import kr.respectme.report.domain.ResourceType
import java.util.UUID

@Schema(description = "신고 생성 응답")
data class ReportCommandResponse(
    @Schema(description = "신고 ID", example = "1")
    val reportId: Long,
    @Schema(description = "신고자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    val reportMemberId: UUID,
    @Schema(description = "신고 대상 리소스", example = "NOTIFICATION")
    val resourceType: ResourceType,
    @Schema(description = "신고 대상 리소스 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    val resourceId: UUID,
    @Schema(description = "신고 처리 상태", example = "REPORTED")
    val reportStatus: ReportStatus,
    @Schema(description = "신고 내용", example = "신고 내용입니다.")
    val reportContent: String,
) {

    companion object {
        fun valueOf(commandModelDto: ReportCommandModelDto): ReportCommandResponse {

            return ReportCommandResponse(
                reportId = commandModelDto.id,
                reportMemberId = commandModelDto.reportMemberId,
                resourceType = commandModelDto.resourceType,
                resourceId = commandModelDto.resourceId,
                reportStatus = commandModelDto.status,
                reportContent = commandModelDto.content
            )
        }
    }
}