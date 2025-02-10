package kr.respectme.group.port.out.msa.file.dto

import kr.respectme.group.adapter.out.persistence.GroupInfoVo
import kr.respectme.group.adapter.out.persistence.entity.NotificationGroupQueryModel


data class LoadImagesRequest(
    val imageIds: List<Long>
) {

    companion object {

        fun fromGroupModels(groupQueryModels: List<NotificationGroupQueryModel>): LoadImagesRequest {
            val ids = groupQueryModels.mapNotNull{it.groupThumbnail}.toList()
            return LoadImagesRequest(
                imageIds = ids
            )
        }

        fun valueOf(groupQueryModel: NotificationGroupQueryModel): LoadImagesRequest {
            val ids = groupQueryModel.groupThumbnail?.let{listOf(it)} ?: emptyList()
            return LoadImagesRequest(
                imageIds = ids
            )
        }

        fun valueOf(groupInfo: GroupInfoVo): LoadImagesRequest {
            val ids = groupInfo.imageId?.let{listOf(it)} ?: emptyList()
            return LoadImagesRequest(
                imageIds = ids
            )
        }

        fun fromGroupInfos(groupInfos: List<GroupInfoVo>): LoadImagesRequest {
            val ids = groupInfos.mapNotNull{it.imageId}.toList()
            return LoadImagesRequest(
                imageIds = ids
            )
        }
    }
}