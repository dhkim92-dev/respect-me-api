package kr.respectme.common.advice.hateoas

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "HATEOAS 응답 객체",
    title = "HATEOAS 응답 객체"
)
abstract class HateoasResponse(
    @get:Schema(name = "_links", description = "HATEOAS 링크")
    val _links: MutableList<HateoasLink> = mutableListOf()
) {

}