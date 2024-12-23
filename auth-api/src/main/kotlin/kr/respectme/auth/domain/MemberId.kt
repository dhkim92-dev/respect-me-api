package kr.respectme.auth.domain

import jakarta.persistence.Embeddable
import java.util.UUID

@Embeddable
class MemberId(val id: UUID) {

    constructor(): this(UUID.randomUUID())

    companion object {

        fun of(value: UUID): MemberId {
            return MemberId(value)
        }
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false
        return id == (other as MemberId).id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}