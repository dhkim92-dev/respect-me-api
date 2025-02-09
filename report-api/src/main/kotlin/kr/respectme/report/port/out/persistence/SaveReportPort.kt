package kr.respectme.report.port.out.persistence

import kr.respectme.report.domain.ReportEntity

interface SaveReportPort {

    fun save(report: ReportEntity): ReportEntity
}