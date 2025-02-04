package kr.respectme.member.port.out.event

import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.repository.Repository
import java.util.UUID

@org.springframework.stereotype.Repository
interface MemberDeleteTransactionRepository: Repository<MemberDeleteTransaction, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "javax.persistence.lock.timeout", value = "5000"))
    fun findById(transactionId: UUID) : MemberDeleteTransaction?

    fun save(transaction: MemberDeleteTransaction) : MemberDeleteTransaction
}