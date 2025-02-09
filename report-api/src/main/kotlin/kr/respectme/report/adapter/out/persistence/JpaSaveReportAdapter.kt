package kr.respectme.report.adapter.out.persistence

import kr.respectme.report.adapter.out.persistence.repository.ReportRepository
import kr.respectme.report.domain.ReportEntity
import kr.respectme.report.port.out.persistence.SaveReportPort
import org.springframework.stereotype.Repository

@Repository
class JpaSaveReportAdapter(private val reportRepository: ReportRepository): SaveReportPort {

    override fun save(report: ReportEntity): ReportEntity {
        return reportRepository.save(report)
    }
}