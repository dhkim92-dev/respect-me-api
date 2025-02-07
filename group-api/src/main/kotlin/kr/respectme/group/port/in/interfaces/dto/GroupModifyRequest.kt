package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.group.domain.GroupType
import java.util.UUID

@Schema(description = "그룹 수정 요청 객체")
data class GroupModifyRequest(
    @Schema(example = "Group Name", description = "그룹 이름, 수정하지 않는다면 null 또는 field를 제외")
    val name: String?,
    @Schema(example = "Group Description", description = "그룹 설명, 수정하지 않는다면 null 또는 field를 제외")
    val description: String?,
    @Schema(example = "UUID", description = "그룹 소유자, 수정하지 않는다면 null 또는 field를 제외")
    val ownerId: UUID?,
    @Schema(example = "PUBLIC", description = "그룹 타입, 수정하지 않는다면 null 또는 field를 제외")
    val type: GroupType?,
    @Schema(example = "password", description = "그룹 비밀번호, 수정하지 않는다면 null 또는 field를 제외")
    val password: String?,
    @Schema(example = "thumbnail", description = "그룹 썸네일, 썸네일 삭제 시 null 그외 수정은 URL 입력")
    val thumbnail: String?
) {

}