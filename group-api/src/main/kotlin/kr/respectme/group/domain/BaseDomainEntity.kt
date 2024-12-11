package kr.respectme.group.domain

import com.fasterxml.jackson.annotation.JsonIgnore

abstract class BaseDomainEntity {

    @JsonIgnore
    private var _entityStatus: EntityStatus = EntityStatus.NEW

    val entityStatus: EntityStatus
        @JsonIgnore
        get() = _entityStatus

    private fun changeEntityStatus(status: EntityStatus) {
        when(_entityStatus) {
            EntityStatus.NEW -> {
                if(status == EntityStatus.DELETED) {
                    _entityStatus = status
                }
            }
            EntityStatus.ACTIVE -> {
                if(status == EntityStatus.UPDATED || status == EntityStatus.DELETED) {
                    _entityStatus = status
                }
            }

            EntityStatus.UPDATED -> {
                if(status == EntityStatus.DELETED) {
                    _entityStatus = status
                }
            }

            EntityStatus.DELETED -> {
                // do nothing
            }
        }
        _entityStatus = status
    }

    fun isNew(): Boolean {
        return entityStatus == EntityStatus.NEW
    }

    fun isUpdated(): Boolean {
        return entityStatus == EntityStatus.UPDATED
    }

    fun isRemoved(): Boolean {
        return entityStatus == EntityStatus.DELETED
    }

    fun isLoaded(): Boolean {
        return entityStatus == EntityStatus.ACTIVE
    }

    fun created() {
        changeEntityStatus(EntityStatus.NEW)
    }

    fun loaded() {
        changeEntityStatus(EntityStatus.ACTIVE)
    }

    fun updated() {
        changeEntityStatus(EntityStatus.UPDATED)
    }

    fun removed() {
        changeEntityStatus(EntityStatus.DELETED)
    }
}