package kr.respectme.report.port.`in`.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import kr.respectme.report.domain.ResourceType
import org.hibernate.validator.constraints.Length
import java.util.UUID

@Schema(description = "신고 생성 요청")
data class CreateReportRequest(
    @Schema(description = "신고 대상 리소스", example = "NOTIFICATION")
    val resourceType: ResourceType,
    @Schema(description = "신고 대상 리소스 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    val resourceId: UUID,
    @Schema(description = "신고 내용", example = "신고 내용입니다.")
    @field: Length(max = 500, message = "신고 내용은 500자 이내로 작성해주세요.")
    @field: NotBlank(message = "신고 내용을 입력해주세요.")
    val content: String
)
