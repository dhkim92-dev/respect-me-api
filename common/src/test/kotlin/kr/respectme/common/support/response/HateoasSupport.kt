package kr.respectme.common.support.response

import kr.respectme.common.advice.hateoas.*
import java.util.*

class SampleHateoasConverter : AbstractHateoasConverter<SampleHateoasElement>() {

    override fun translate(element: SampleHateoasElement): List<HateoasLink> {
        return listOf(
            HateoasLink(
                rel = "self",
                href = "https://www.noti-me.net/samples/${element.id}",
            )
        )
    }
}

@Hateoas(converter = SampleHateoasConverter::class)
class SampleHateoasElement(
    val id: UUID
) : HateoasResponse() {

}

@Hateoas(converter = InnerElementHateoasConverter::class)
class InnerElement(
    val id: UUID,
) : HateoasResponse() {

}

@Hateoas(converter = OutterElementHateoasConverter::class)
class OutterElement(
    val id : UUID,
    val inner: List<InnerElement>,
): HateoasResponse() {

}

class OutterElementHateoasConverter : AbstractHateoasConverter<OutterElement>() {

    override fun translate(element: OutterElement): List<HateoasLink> {
        element._links.addAll(
            listOf(
                HateoasLink(rel = "self",
                    href = "https://www.noti-me.net/api/v1/outters/${element.id}",
                ),
                HateoasLink(rel = "delete",
                    href = "https://www.noti-me.net/api/v1/outters/${element.id}",
                )
            )
        )

        return element._links
    }
}

class InnerElementHateoasConverter : AbstractHateoasConverter<InnerElement>() {

    override fun translate(element: InnerElement): List<HateoasLink> {
        element._links.addAll(
            listOf(
                HateoasLink(rel = "self",
                    href = "https://www.noti-me.net/api/v1/inners/${element.id}",
                )
            )
        )

        return element._links
    }
}

