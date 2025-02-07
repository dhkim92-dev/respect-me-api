package kr.respectme.file.adapter.`in`.interfaces

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import kr.respectme.common.annotation.LoginMember
import kr.respectme.common.error.NotFoundException
import kr.respectme.common.error.UnsupportedMediaTypeException
import kr.respectme.file.application.dto.ImageFileCreateCommand
import kr.respectme.file.application.usecase.ThumbnailUseCase
import kr.respectme.file.common.annotation.ImageBytes
import kr.respectme.file.common.errors.FileErrorCode
import kr.respectme.file.domain.enum.ImageFormat
import kr.respectme.file.domain.enum.ImageType
import kr.respectme.file.port.`in`.interfaces.ThumbnailPort
import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailUploadResponse
import kr.respectme.file.port.`in`.interfaces.vo.FileOwner
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/api/v1/files")
class RestImageFileUploadAdapter(
    private val thumbnailUseCase: ThumbnailUseCase
): ThumbnailPort {

    @Operation(summary = "이미지 파일을 업로드한다", description = "이미지 파일 업로드")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "업로드 성공"),
    ])
    @PutMapping("/images", consumes = ["multipart/form-data"], produces = ["application/json"])
    @SecurityRequirement(name = "bearer-jwt")
    override fun createThumbnail(
        @LoginMember
        loginId: UUID,
        @Schema(description = "이미지 파일", required = true, type = "string", format = "binary")
        @RequestPart(required = true)
        @ImageBytes
        image: MultipartFile
    ): ThumbnailUploadResponse {
        return ThumbnailUploadResponse.valueOf(
            thumbnailUseCase.createThumbnail(loginId, command = ImageFileCreateCommand.of(image))
        )
    }
}