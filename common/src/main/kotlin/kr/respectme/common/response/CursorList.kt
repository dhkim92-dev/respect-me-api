package kr.respectme.common.response

data class CursorList<T>(
    val count: Int,
    val data: List<T> = listOf(),
    val next: String? = null
) {

    companion object {
        fun <T> of(data: List<T>, next: String?, pageSize: Int): CursorList<T> {
            if (data.isEmpty()) {
                return CursorList<T>(0, emptyList(), null)
            }
//            println("data size : ${data.size}")
            var count: Int = if (pageSize + 1 <= data.size) pageSize else data.size

            return CursorList<T>(
                count = count,
                data = data.subList(0, count),
                next = if(data.size > count) next else null
            )
        }
    }

}