package kr.respectme.group.port.`in`.interfaces.dto

import kr.respectme.common.advice.hateoas.*
import kr.respectme.group.application.attachment.AttachmentDto
import kr.respectme.group.configs.MsaConfig
import kr.respectme.group.domain.attachment.AttachmentType
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AttachmentResponseConverter(
    private val msaConfig: MsaConfig
) : AbstractHateoasConverter<AttachmentResponse>() {

    companion object {
        const val FILE_API_ENDPOINT = "api/v1/files/group-shared"
    }

    override fun translate(element: AttachmentResponse) {
        element._links.addAll(listOf(
            getSelf(element),
            getResource(element)
        ))
    }

    private fun getSelf(element: AttachmentResponse) : HateoasLink {
        return HateoasLink(
            rel = "self",
            href = "${msaConfig.getGatewayUrl()}/api/v1/notification-groups/${element.groupId}/notifications/${element.notificationId}/attachments/${element.id}"
        )
    }

    private fun getResource(element: AttachmentResponse) : HateoasLink {
        val endpoint = getEndpoint(element.type)
        return HateoasLink(
            rel = "resource",
            href = "${msaConfig.getGatewayUrl()}/${getEndpoint(element.type)}/${element.resourceId}"
        )
    }

    private fun getEndpoint(type: AttachmentType): String {
        return when(type) {
            AttachmentType.FILE -> FILE_API_ENDPOINT
            else -> throw IllegalArgumentException("Not supported attachment type")
        }
    }
}

@Hateoas(converter = AttachmentResponseConverter::class)
data class AttachmentResponse(
    val id: UUID,
    val groupId: UUID,
    val notificationId: UUID,
    val resourceId: UUID,
    val type: AttachmentType,
): HateoasResponse() {

    companion object {
        fun of(dto: AttachmentDto): AttachmentResponse {
            return AttachmentResponse(
                id = dto.id,
                type = dto.type,
                groupId = dto.groupId,
                notificationId = dto.notificationId,
                resourceId = dto.resourceId,
            )
        }
    }
}