package kr.respectme.group.application.query

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.group.application.dto.group.GroupQueryModelDto
import kr.respectme.group.application.dto.group.GroupSearchParams
import kr.respectme.group.application.query.useCase.NotificationGroupQueryUseCase
import kr.respectme.group.common.errors.GroupServiceErrorCode.*
import kr.respectme.group.domain.GroupType
import kr.respectme.group.port.out.msa.file.LoadImagePort
import kr.respectme.group.port.out.msa.file.dto.LoadImagesRequest
import kr.respectme.group.port.out.persistence.LoadMemberPort
import kr.respectme.group.port.out.persistence.LoadNotificationPort
import kr.respectme.group.port.out.persistence.QueryGroupPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class NotificationGroupQueryService(
    private val queryGroupPort: QueryGroupPort,
    private val loadMemberPort: LoadMemberPort,
    private val loadNotificationPort: LoadNotificationPort,
    private val loadImagePort: LoadImagePort
): NotificationGroupQueryUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun retrieveGroup(loginId: UUID, groupId: UUID): GroupQueryModelDto {
        val group = queryGroupPort.getGroup(loginId, groupId)
            ?: throw NotFoundException(GROUP_NOT_FOUND)

        if (group.type == GroupType.GROUP_PRIVATE) {
            val groupMember = loadMemberPort.load(groupId, loginId)
                ?: throw ForbiddenException(GROUP_MEMBER_NOT_MEMBER)
        }

        val thumbnailId = group.groupThumbnail
        val imageQueryResponse = loadImagePort.getImageInfos(LoadImagesRequest(listOf(thumbnailId!!)))

        logger.debug("imageQueryResponse ${jacksonObjectMapper().writeValueAsString(imageQueryResponse)}")

        return GroupQueryModelDto.valueOf(group, imageQueryResponse.data.firstOrNull()?.url)
    }

    @Transactional(readOnly = true)
    override fun retrieveGroupsBySearchParam(
        searchParams: GroupSearchParams,
        cursor: UUID?,
        size: Int?
    ): List<GroupQueryModelDto> {
        val queryResult = queryGroupPort.getGroupsByNameContainsKeyword(
            searchParams.keyword,
            cursor,
            size?.let{it + 1} ?: 21
        )
        val thumbnailMap = loadImagePort.getImageInfos(LoadImagesRequest(queryResult.filter{it
            it.groupThumbnail != null
        }.map{it -> it.groupThumbnail!!}
        .toList()))

        return queryResult.map{ group ->
            GroupQueryModelDto.valueOf(group, thumbnailMap?.data?.firstOrNull{it.id == group.groupThumbnail}?.url)
        }
    }

    @Transactional(readOnly = true)
    override fun retrieveMemberGroups(loginId: UUID): List<GroupQueryModelDto> {
        val queryResult = queryGroupPort.getMemberGroups(loginId)
        val thumbnailMap = loadImagePort.getImageInfos(
            LoadImagesRequest(queryResult.filter{it.groupThumbnail != null}
            .map{it -> it.groupThumbnail!!}
            .toList())
        )

        return queryResult.map{ group ->
            GroupQueryModelDto.valueOf(group, thumbnailMap?.data?.firstOrNull{it.id == group.groupThumbnail}?.url)
        }
    }
}