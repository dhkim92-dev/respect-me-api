package kr.respectme.report.application.impl

import kr.respectme.report.application.dto.CreateReportCommand
import kr.respectme.report.application.dto.ReportCommandModelDto
import kr.respectme.report.application.usecase.ReportCommandUseCase
import kr.respectme.report.domain.ReportDetailEntity
import kr.respectme.report.port.out.persistence.LoadReportPort
import kr.respectme.report.port.out.persistence.SaveReportPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ReportCommandService(
    private val loadReportPort: LoadReportPort,
    private val saveReportPort: SaveReportPort
): ReportCommandUseCase {

    @Transactional
    override fun createReport(loginId: UUID, command: CreateReportCommand): ReportCommandModelDto {
        val report = CreateReportCommand.toEntity(loginId, command)
        val details = ReportDetailEntity(content = command.content)
        report.addReportDetail(details)
        saveReportPort.save(report)

        return ReportCommandModelDto.valueOf(report)
    }
}