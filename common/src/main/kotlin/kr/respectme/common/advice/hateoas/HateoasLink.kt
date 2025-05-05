package kr.respectme.common.advice.hateoas

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "HATEOAS Link",
    title = "HATEOAS Link"
)
class HateoasLink(
    @Schema(
        description = "관계",
        title = "원본과의 관계",
        example = "self"
    )
    val rel: String,
    @Schema(
        description = "링크",
        title = "링크",
        example = "https://www.noti-me.net/api/v1/members/{memberId}",
    )
    val href: String?,
) {
}