package kr.respectme.common.domain.entity.success

import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.domain.annotations.DomainRelation

@DomainEntity
class TestPassEntity(
    id: Long = 1,
    var name: String = ""
): BaseDomainEntity<Long>(id) {

    @DomainRelation
    var relations: List<TestPassUUIDEntity> = listOf()
}