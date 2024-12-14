package kr.respectme.common.domain.entity.success

import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import java.util.*

@DomainEntity
class TestPassUUIDEntity(id : UUID): BaseDomainEntity<UUID>(id) {
}