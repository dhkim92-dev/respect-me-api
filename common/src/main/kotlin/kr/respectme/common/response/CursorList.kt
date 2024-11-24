package kr.respectme.common.response

import com.fasterxml.jackson.annotation.JsonIgnore
import org.slf4j.LoggerFactory

data class CursorList<T>(
    val count: Int,
    val data: List<T> = listOf(),
    val next: String? = null
) {


    companion object {
        private val logger = LoggerFactory.getLogger(javaClass)

        fun <T> of(data: List<T>, next: String?, pageSize: Int): CursorList<T> {
            if (data.isEmpty()) {
                return CursorList<T>(0, emptyList(), null)
            }
//            println("data size : ${data.size}")
            logger.debug("data size: ${data.size}")

            var count: Int = if (pageSize + 1 <= data.size) pageSize else data.size

            logger.debug("count : ${count}")
            logger.debug("next : ${next}")

            return CursorList<T>(
                count = count,
                data = data.subList(0, count),
                next = if(data.size > count) next else null
            )
        }
    }

}