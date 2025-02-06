package port.`in`.interfaces.dto

//import common.annotation.MultipartFileImageValidate
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Schema(name = "GroupThumbnailCreateRequest", description = "그룹 썸네일 생성 요청 객체")
data class GroupThumbnailCreateRequest(
    @Schema(name = "Group ID", description = "Group ID", required = true)
    val groupId: UUID,
    @Schema(name = "Group Thumbnail 이미지 파일", description = "Group Thumbnail 이미지 파일(jpg, png 지원)", required = true)
//    @MultipartFileImageValidate
    val file: MultipartFile
)