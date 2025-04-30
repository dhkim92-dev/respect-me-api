package kr.respectme.file.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Schema(description = "그룹 파일 업로드 요청, multipart/form-data")
data class GroupFileUploadRequest(
    @Schema(description = "업로드 대상 그룹 ID")
    val groupId: UUID,
    @Schema(description = "대상 파일, 최대 1MB, 현재 (jpg, png) 지원")
    val file: MultipartFile
)