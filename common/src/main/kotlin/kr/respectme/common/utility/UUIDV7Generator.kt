package kr.respectme.common.utility

import java.util.UUID
import java.util.concurrent.ThreadLocalRandom


/**
 * RFC9562 UUID V7 Spec Implementation.
 * @link https://datatracker.ietf.org/doc/rfc9562/
 * MostSigBits(64bits) first 48bits Unix Timestamp,
 * Next 4bits version 0x7000L
 * Next 12bits random value
 * LeastSigBits(64bits) two bits are 0b01
 * Next 62bits random value
 */
object UUIDV7Generator {

    fun generate(): UUID {
        val random = ThreadLocalRandom.current()

        val currentTimeMillis = System.currentTimeMillis()
        val ts = (currentTimeMillis and 0xFFFFFFFFFFFFL) shl 16
        val version = ts or 0x7000L
        val randomHigh = random.nextLong(0, 1L shl 12)
        val mostSigBits = ts or version or randomHigh
        val leastSigBits = (0b10L shl 62) or random.nextLong(0, 1L shl 62)

        return UUID(mostSigBits, leastSigBits)
    }
}