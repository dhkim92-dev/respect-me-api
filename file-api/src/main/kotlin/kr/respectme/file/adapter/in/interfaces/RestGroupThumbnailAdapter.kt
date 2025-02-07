package kr.respectme.file.adapter.`in`.interfaces

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.LoginMember
import kr.respectme.file.annotation.ThumbnailImageValidate
import kr.respectme.file.port.`in`.interfaces.GroupThumbnailPort
import kr.respectme.file.port.`in`.interfaces.dto.GroupThumbnailCreateRequest
import kr.respectme.file.port.`in`.interfaces.dto.GroupThumbnailCreateResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID
import javax.swing.GroupLayout.Group

@RestController
@RequestMapping("/api/v1/files/")
class RestGroupThumbnailAdapter(
){
//): GroupThumbnailPort {

    @Operation(summary = "그룹 썸네일 업로드", description = "그룹 썸네일을 등록 합니다.<br/>" +
            "요청은 multipart/form-data 형식으로 해야 하며, <br/>" +
            "file 필드에 이미지 파일을 넣어 주세요.<br/>" +
            "file 필드는 반드시 image/jpeg, image/png, image/jpg 타입이어야 합니다.<br/>" +
            "이미지의 크기는 128 x 128로 고정되어야 합니다."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "그룹 썸네일 변경 성공"),
    ])
    @PutMapping("images/group-thumbnails/{groupId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
//    override fun uploadGroupThumbnail(
    @ApplicationResponse(status = HttpStatus.OK, message = "그룹 썸네일 변경 성공")
    fun uploadGroupThumbnail(
//        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @RequestPart file: MultipartFile)
    : GroupThumbnailCreateResponse {
        return GroupThumbnailCreateResponse(0, "", "")
    }

    @Operation(summary = "그룹 썸네일 이미지 삭제.", description = "")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "그룹 썸네일 삭제 성공"),
    ])
    @DeleteMapping("images/group-thumbnails/{groupId}")
//    override fun deleteGroupThumbnail(
    fun deleteGroupThumbnail(
//        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID
    ) {

    }
}