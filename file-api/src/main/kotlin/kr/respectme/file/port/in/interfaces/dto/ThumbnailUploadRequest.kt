package kr.respectme.file.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.file.common.annotation.ImageBytes
import org.springframework.web.multipart.MultipartFile

@Schema(description = "썸네일 업로드 요청, Multipart/form data로 전송")
class ThumbnailUploadRequest(
    @Schema(description = "이미지 파일", required = true, type = "string", format = "binary")
    @field: ImageBytes(message = "jpg, png 파일만 지원합니다.")
    val image: MultipartFile
){
}