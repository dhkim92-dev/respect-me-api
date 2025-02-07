package kr.respectme.file.port.`in`.interfaces.dto

//import common.annotation.MultipartFileImageValidate
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

//@Schema(name = "GroupThumbnailCreateRequest", description = "그룹 썸네일 생성 요청 객체")
data class GroupThumbnailCreateRequest(
//    @Schema(description = "Thumbnail을 바꿀 Group ID", required = true)
    val groupId: UUID,
)