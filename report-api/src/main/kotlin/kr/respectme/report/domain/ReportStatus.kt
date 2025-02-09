package kr.respectme.report.domain

enum class ReportStatus(val value: Int) {
    REPORTED(1),
    PROCESSED(2),
    REJECTED(3)
}