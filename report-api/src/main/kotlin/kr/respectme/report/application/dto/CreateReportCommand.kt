package kr.respectme.report.application.dto

import kr.respectme.report.domain.ReportEntity
import kr.respectme.report.domain.ResourceType
import kr.respectme.report.port.`in`.dto.CreateReportRequest
import org.apache.kafka.clients.consumer.internals.ConsumerNetworkThread
import java.util.UUID

data class CreateReportCommand(
    val resourceType: ResourceType,
    val resourceId: UUID,
    val content: String
) {

    companion object {
        fun valueOf(request: CreateReportRequest): CreateReportCommand {
            return CreateReportCommand(
                resourceType = request.resourceType,
                resourceId = request.resourceId,
                content = request.content
            )
        }

        fun toEntity(loginId: UUID, command: CreateReportCommand): ReportEntity {
            return ReportEntity(
                reportMemberId = loginId,
                resourceType = command.resourceType,
                resourceId = command.resourceId,
            )
        }
    }
}
