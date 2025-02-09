package kr.respectme.report.adapter.out.persistence.repository

import kr.respectme.report.domain.ReportEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository : JpaRepository<ReportEntity, Long> {
}