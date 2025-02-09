package kr.respectme.report.adapter.out.persistence

import kr.respectme.report.adapter.out.persistence.repository.ReportRepository
import kr.respectme.report.domain.ReportEntity
import kr.respectme.report.port.out.persistence.LoadReportPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaLoadReportAdapter(private val reportRepository: ReportRepository): LoadReportPort {

    override fun load(id: Long): ReportEntity? {
        return reportRepository.findByIdOrNull(id)
    }
}