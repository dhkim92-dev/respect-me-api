package kr.respectme.group.domain

enum class EntityStatus(val value: Int){

    NEW(0x1),
    ACTIVE(0x2),
    UPDATED(0x4),
    DELETED(0x8)
}