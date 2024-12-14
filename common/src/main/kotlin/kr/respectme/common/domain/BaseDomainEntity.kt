package kr.respectme.common.domain

abstract class BaseDomainEntity<T>(
    id: T
) {

    val id: T = id

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as BaseDomainEntity<*>

        if (id != other.id) return false

        return true
    }
}