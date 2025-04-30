package kr.respectme.file.adapter.`in`.interfaces

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.LoginMember
import kr.respectme.file.application.dto.ImageFileCreateCommand
import kr.respectme.file.application.usecase.ThumbnailUseCase
import kr.respectme.file.port.`in`.interfaces.ThumbnailPort
import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailUploadRequest
import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailUploadResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "File API", description = "파일 CRUD API 모음")
class RestImageFileUploadAdapter(
    private val thumbnailUseCase: ThumbnailUseCase
): ThumbnailPort {

    @Operation(summary = "이미지 파일을 업로드한다", description = "이미지 파일 업로드")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "업로드 성공"),
    ])
    @PutMapping("/images", consumes = ["multipart/form-data"], produces = ["application/json"])
    @SecurityRequirement(name = "bearer-jwt")
    @ApplicationResponse(
        status = HttpStatus.OK,
        message = "업로드 성공"
    )
    override fun createThumbnail(
        @LoginMember
        loginId: UUID,
        @ModelAttribute
        @Valid request: ThumbnailUploadRequest
    ): ThumbnailUploadResponse {
        return ThumbnailUploadResponse.valueOf(
            thumbnailUseCase.createThumbnail(loginId, command = ImageFileCreateCommand.of(request.image))
        )
    }
}