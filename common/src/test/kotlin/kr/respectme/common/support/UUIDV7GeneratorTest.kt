package kr.respectme.common.support


import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class UUIDV7GeneratorTest {

    class TestCase(
        val uuid: UUID,
        val createdAt: Instant
    ) {

    }

    fun generate(ts: Instant): UUID {
        val random = ThreadLocalRandom.current()

//        val currentTimeMillis = System.currentTimeMillis()
        val currentTimeMillis = ts.toEpochMilli()
        val ts = (currentTimeMillis and 0xFFFFFFFFFFFFL) shl 16
        val version = ts or 0x7000L
        val randomHigh = random.nextLong(0, 1L shl 12)
//        val mostSigBits = ts or version or (random.nextLong(0, 1L shl 12))
        val mostSigBits = ts or version or randomHigh
        val leastSigBits = (0b10L shl 62) or random.nextLong(0, 1L shl 62)

        return UUID(mostSigBits, leastSigBits)
    }

    @Test
    fun `UUIDV7 정렬 테스트`() {
        val data = arrayOf(
            "0195235f-2836-71b3-8b00-9a3fa7f8499f|2025-02-20T12:39:53.656119Z",
            "019518a1-7a94-7210-97d6-fc73f93d2183|2025-02-18T10:38:33.867342Z",
            "0195235f-c103-7802-93b0-abf239860061|2025-02-20T12:40:32.772821Z",
            "01951969-b40a-772c-81fc-437cf713fa06|2025-02-18T14:15:12.652624Z",
            "019518a5-3da6-7972-9f7a-753461194b02|2025-02-19T15:17:36.877209Z",
            "019527d8-161c-768a-a519-ca239c2e33a4|2025-02-21T09:30:27.744567Z",
            "01952007-e64a-7ae7-8169-9f6dd826f3ad|2025-02-19T21:05:43.503477Z",
        )

        val tcs = data.map {
            val (uuid, createdAt) = it.split("|")
            val ts = Instant.parse(createdAt)
            TestCase(generate(ts), ts)
        }

        val sorted = tcs.sortedBy { it.uuid }

        sorted.forEach {
            println("${it.uuid}|${it.createdAt}")
        }
    }

}