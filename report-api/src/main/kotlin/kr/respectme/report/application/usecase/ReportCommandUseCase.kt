package kr.respectme.report.application.usecase

import kr.respectme.report.application.dto.CreateReportCommand
import kr.respectme.report.application.dto.ReportCommandModelDto
import java.util.UUID

interface ReportCommandUseCase {

    fun createReport(loginId: UUID, command: CreateReportCommand): ReportCommandModelDto
}