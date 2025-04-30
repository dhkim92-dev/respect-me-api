package kr.respectme.file.adapter.`in`.interfaces

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.LoginMember
import kr.respectme.common.error.UnsupportedMediaTypeException
import kr.respectme.file.application.dto.GroupFileUploadCommand
import kr.respectme.file.application.usecase.GroupFileQueryUsecase
import kr.respectme.file.application.usecase.GroupFileUsecase
import kr.respectme.file.common.errors.FileErrorCode
import kr.respectme.file.port.`in`.interfaces.GroupSharedFilePort
import kr.respectme.file.port.`in`.interfaces.dto.GroupFileQueryResponse
import kr.respectme.file.port.`in`.interfaces.dto.GroupFileUploadRequest
import kr.respectme.file.port.`in`.interfaces.dto.GroupFileUploadResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "그룹 공유 파일 API", description = "그룹 공유 파일 API")
class RestGroupSharedFileUploadAdapter(
    private val fileUsecases: List<GroupFileUsecase>,
    private val fileQueryUsecase: GroupFileQueryUsecase
): GroupSharedFilePort {

    @PutMapping("/group-files", consumes = ["multipart/form-data"], produces = ["application/json"])
    @ApiResponse(responseCode = "200", description = "업로드 성공")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "그룹 공유 파일을 업로드한다", description = "그룹 공유 파일 업로드")
    @ApplicationResponse(
        status = HttpStatus.OK,
        message = "업로드 성공"
    )
    override fun uploadImage(
        @LoginMember
        memberId: UUID,
        @ModelAttribute
        request: GroupFileUploadRequest)
    : GroupFileUploadResponse {
        val usecase = fileUsecases.firstOrNull { it.isSupportFormat(request.file) }
            ?: throw UnsupportedMediaTypeException(FileErrorCode.FileFormatNotSupported)
        val result = usecase.upload(
            memberId = memberId,
            command = GroupFileUploadCommand.of(request)
        )
        return GroupFileUploadResponse.of(result)
    }

    @GetMapping("/group-files/{fileId}")
    @ApiResponse(responseCode = "200", description = "파일 조회 성공")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "그룹 공유 파일을 조회한다", description = "그룹 공유 파일 조회, 실제 파일 엑세스를 위한 Signed URL을 반환합니다.")
    @ApplicationResponse(
        status = HttpStatus.OK,
        message = "파일 조회 성공"
    )
    override fun getGroupFile(@LoginMember loginId: UUID, @PathVariable fileId: UUID)
    : GroupFileQueryResponse {
        val result = fileQueryUsecase.getGroupFile(loginId, fileId)
        return GroupFileQueryResponse.of(result)
    }
}