package kr.respectme.report.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import kr.respectme.report.domain.converter.ReportStatusConverter
import kr.respectme.report.domain.converter.ResourceTypeConverter
import java.util.LinkedHashSet
import java.util.UUID

@Entity
@Table(name = "report")
class ReportEntity(
    id: Long? = null,
    @Convert(converter = ResourceTypeConverter::class)
    val resourceType: ResourceType,
    @Column
    val reportMemberId: UUID,
    @Column
    val resourceId: UUID,
    @Convert(converter = ReportStatusConverter::class)
    var status: ReportStatus = ReportStatus.REPORTED,
    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val reportDetails: MutableSet<ReportDetailEntity> = LinkedHashSet(),
//    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
//    val reportResponses: List<ReportResponseEntity>
): LongBaseEntity(id) {

    fun addReportDetail(reportDetail: ReportDetailEntity) {
        reportDetails.add(reportDetail)
        reportDetail.report = this
    }
}