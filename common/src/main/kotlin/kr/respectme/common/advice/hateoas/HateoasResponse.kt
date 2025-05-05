package kr.respectme.common.advice.hateoas

abstract class HateoasResponse(
    override val _links: MutableList<HateoasLink> = mutableListOf()
) : Hateoasable {

}