package kr.respectme.report.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "report_detail")
class ReportDetailEntity(
    id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    var report: ReportEntity? = null,
    @Column(length = 500)
    var content: String
): LongBaseEntity(id) {

}