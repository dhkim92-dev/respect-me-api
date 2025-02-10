package kr.respectme.file.adapter.`in`.interfaces

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.ServiceAccount
import kr.respectme.file.application.dto.ImagesQuery
import kr.respectme.file.application.usecase.ThumbnailUseCase
import kr.respectme.file.port.`in`.interfaces.ThumbnailQueryPort
import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailQueryResponse
import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailsQueryRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("")
@Tag(name = "Image Query API", description = "이미지 정보를 조회하는 API")
class RestImageQueryAdapter(
    private val thumbnailUseCase: ThumbnailUseCase,
): ThumbnailQueryPort {

    @PostMapping("internal/api/v1/files/images")
    @ApplicationResponse(status = HttpStatus.OK, message = "주어진 아이디 목록의 이미지 정보를 조회 했습니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공"),
        ]
    )
    @Operation(summary = "이미지 정보 조회", description = "주어진 아이디 목록의 이미지 정보를 조회합니다.<br/> 이 엔드포인트는 서비스 전용입니다.")
    override fun getImages(
        @ServiceAccount serviceAccount: UUID,
        @RequestBody request: ThumbnailsQueryRequest)
    : List<ThumbnailQueryResponse> {
        return thumbnailUseCase.getThumbnails(ImagesQuery.valueOf(request))
            .map{ThumbnailQueryResponse.valueOf(it)}
    }
}