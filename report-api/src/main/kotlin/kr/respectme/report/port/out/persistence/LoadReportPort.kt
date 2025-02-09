package kr.respectme.report.port.out.persistence

import kr.respectme.report.domain.ReportEntity

interface LoadReportPort {

    fun load(id: Long): ReportEntity?
}