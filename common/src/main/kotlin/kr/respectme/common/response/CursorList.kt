package kr.respectme.common.response

class CursorList<T>(
    count: Int,
    data: List<T> = listOf(),
    next: String? = null
) {

    companion object {
        fun <T> of(data: List<T>, next: String?, pageSize: Int): CursorList<T> {
            if (data.isEmpty()) {
                return CursorList<T>(0, emptyList(), null)
            }
            var count: Int = if (pageSize + 1 <= data.size) pageSize else data.size
            return CursorList<T>(count, data.subList(0, count), next)
        }
    }

}