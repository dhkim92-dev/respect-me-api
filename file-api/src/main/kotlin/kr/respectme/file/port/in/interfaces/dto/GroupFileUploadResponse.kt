package kr.respectme.file.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.common.advice.hateoas.AbstractHateoasConverter
import kr.respectme.common.advice.hateoas.Hateoas
import kr.respectme.common.advice.hateoas.HateoasLink
import kr.respectme.common.advice.hateoas.HateoasResponse
import kr.respectme.file.application.dto.GroupFileDto
import kr.respectme.file.configs.MsaConfig
import kr.respectme.file.domain.enums.FileFormat
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GroupFileUploadResponseConverter(
    private val msaConfig: MsaConfig
): AbstractHateoasConverter<GroupFileUploadResponse>() {

    override fun translate(element: GroupFileUploadResponse) {
        element._links.addAll(listOf(
            HateoasLink(
                rel = "self",
                href = "${msaConfig.getGatewayUrl()}/api/v1/files/group-files/${element.fileId}"
            ),

            HateoasLink(
                rel = "group-files",
                href = "${msaConfig.getGatewayUrl()}/api/v1/files/group-files?groupId=${element.groupId}"
            ),
        ))
    }
}

@Hateoas(converter = GroupFileUploadResponseConverter::class)
@Schema(description = "그룹 파일 업로드 응답")
data class GroupFileUploadResponse(
    @Schema(description = "업로드 된 이미지 식별자, 이미지 접근 URL 확보를 위해서는 해당 URL로 이미지 조회 요청 필요", example = "d67f4dfe-0d0f-406c-be67-71205404a013")
    val fileId: UUID,
    @Schema(description = "업로드 된 파일의 소유 그룹 ID", example = "a6bfcdfe-0d0f-406c-be67-abcde404a714")
    val groupId: UUID,
    @Schema(description = "업로드 된 파일의 소유자 ID", example = "b7bfcdfe-0d0f-406c-be67-abcde404a714")
    val uploaderId: UUID,
    @Schema(description = "업로드 된 파일의 원본 이름", example = "image.png")
    val fileName: String,
    @Schema(description = "업로드 된 파일의 크기", example = "1024")
    val size: Long,
    @Schema(description = "업로드 된 파일의 포맷", example = "JPEG")
    val fileFormat: FileFormat,
    @Schema(description = "업로드 된 파일의 접근 경로", example = "/private/d/6/d67f4dfe-0d0f-406c-be67-71205404a013.png")
    val path: String
) : HateoasResponse(){

    companion object {
        fun of( dto: GroupFileDto ): GroupFileUploadResponse {
            return GroupFileUploadResponse(
                fileId = dto.fileId,
                groupId = dto.groupId,
                uploaderId = dto.uploaderId,
                fileName = dto.name,
                size = dto.size,
                fileFormat = dto.fileFormat,
                path = dto.path
            )
        }
    }
}