package kr.respectme.member.port.out.saga

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
    fun findByIdForUpdate(transactionId: UUID) : MemberDeleteTransaction?

    fun save(transaction: MemberDeleteTransaction) : MemberDeleteTransaction
}