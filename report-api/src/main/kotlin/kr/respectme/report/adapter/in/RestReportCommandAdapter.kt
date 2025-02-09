package kr.respectme.report.adapter.`in`

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.LoginMember
import kr.respectme.report.application.dto.CreateReportCommand
import kr.respectme.report.application.usecase.ReportCommandUseCase
import kr.respectme.report.port.`in`.ReportCommandPort
import kr.respectme.report.port.`in`.dto.CreateReportRequest
import kr.respectme.report.port.`in`.dto.ReportCommandResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "신고 API", description = "리소스에 대한 신고를 처리하는 API")
@SecurityRequirement(name = "bearer-jwt")
class RestReportCommandAdapter(
    private val reportCommandUseCase: ReportCommandUseCase
): ReportCommandPort {

    @Operation(summary = "신고 생성 요청", description = "리소스에 대한 신고를 생성합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "신고가 성공적으로 생성되었습니다."),
    ])
    @PostMapping("")
    @ApplicationResponse(HttpStatus.CREATED, "신고가 접수 되었습니다.")
    override fun createReport(@LoginMember loginId: UUID, @RequestBody @Valid request: CreateReportRequest)
    : ReportCommandResponse {
        return ReportCommandResponse.valueOf(reportCommandUseCase.createReport(loginId, CreateReportCommand.valueOf(request)))
    }
}