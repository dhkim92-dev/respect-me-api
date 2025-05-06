package kr.respectme.common.advice.hateoas

interface HateoasConverter {

    fun convert(element: HateoasResponse): HateoasResponse
}