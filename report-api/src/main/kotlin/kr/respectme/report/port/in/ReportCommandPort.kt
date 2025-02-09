package kr.respectme.report.port.`in`

import kr.respectme.report.port.`in`.dto.CreateReportRequest
import kr.respectme.report.port.`in`.dto.ReportCommandResponse
import java.util.UUID

interface ReportCommandPort {

    fun createReport(loginId: UUID, request: CreateReportRequest): ReportCommandResponse
}