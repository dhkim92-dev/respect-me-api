package kr.respectme.group.adapter.out.msa.file

import kr.respectme.common.response.ApiResult
import kr.respectme.group.port.out.msa.file.LoadImagePort
import kr.respectme.group.port.out.msa.file.dto.LoadImageResponse
import kr.respectme.group.port.out.msa.file.dto.LoadImagesRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "file-api", url = "\${respect-me.msa.file-api.url}")
interface FeignLoadImageAdapter: LoadImagePort{

    @PostMapping("/internal/api/v1/files/images")
    override fun getImageInfos(request: LoadImagesRequest): ApiResult<List<LoadImageResponse>>
}