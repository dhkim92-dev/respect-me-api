package kr.respectme.common.advice.hateoas

interface Hateoasable {
    val _links: MutableList<HateoasLink>
}