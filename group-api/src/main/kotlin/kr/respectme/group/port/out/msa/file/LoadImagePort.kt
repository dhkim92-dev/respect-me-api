package kr.respectme.group.port.out.msa.file

import kr.respectme.common.response.ApiResult
import kr.respectme.group.port.out.msa.file.dto.LoadImageResponse
import kr.respectme.group.port.out.msa.file.dto.LoadImagesRequest

interface LoadImagePort {

    fun getImageInfos(request: LoadImagesRequest): ApiResult<List<LoadImageResponse>>
}