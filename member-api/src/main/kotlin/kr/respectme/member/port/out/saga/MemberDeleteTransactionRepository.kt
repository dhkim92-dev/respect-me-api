package kr.respectme.member.port.out.saga

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import java.util.UUID

@org.springframework.stereotype.Repository
interface MemberDeleteTransactionRepository: Repository<MemberDeleteTransaction, UUID> {

    /**
     * 트랜잭션 조회 시 사용, 조작을 해야한다면 사용하지 말 것.
     * @param transactionId
     * @return MemberDeleteTransaction
     */
    fun findById(transactionId: UUID) : MemberDeleteTransaction?

    /***
     * 트랜잭션 상태 업데이트 시에 충돌을 방지하기 위해 비관적 락을 사용.
     * @param transactionId
     * @return MemberDeleteTransaction
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM MemberDeleteTransaction t WHERE t.id = :transactionId")
    fun findByIdForUpdate(transactionId: UUID) : MemberDeleteTransaction?

    fun save(transaction: MemberDeleteTransaction) : MemberDeleteTransaction
}