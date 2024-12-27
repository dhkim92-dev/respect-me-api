package kr.respectme.member.port.out.saga

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import kr.respectme.common.utility.UUIDV7Generator
import java.time.Instant
import java.util.UUID

@Entity
class MemberDeleteTransaction(
    @Id
    @Column(name = "transaction_id")
    private val id: UUID = UUIDV7Generator.generate(),

    @Column
    private val memberId: UUID = UUID.randomUUID(),

    @Column(name = "transaction_status")
    private var status: TransactionStatus = TransactionStatus.PENDING,

    @Column
    private var createdAt: Instant = Instant.now(),

    @Column
    private var authServiceStatus: TransactionStatus = TransactionStatus.PENDING,

    @Column
    private var groupServiceStatus: TransactionStatus = TransactionStatus.PENDING
) {

    fun getId() = this.id
    fun getStatus() = this.status
    fun getCreatedAt() = this.createdAt
    fun getAuthServiceCompleted() = this.authServiceStatus
    fun getGroupServiceCompleted() = this.groupServiceStatus

    fun setStatus(status: TransactionStatus) {
        if(this.status == TransactionStatus.COMPLETED) {
            // TODO: 메시지 중복 처리를 막으려면 에러를 발생 시키는게 맞는지 확인
            return
        }
        this.status = status
    }

    fun setAuthServiceCompleted(status: TransactionStatus) {
        if(authServiceStatus == TransactionStatus.PENDING) {
            this.authServiceStatus = status
        }
    }

    fun setGroupServiceCompleted(status: TransactionStatus) {
        if(groupServiceStatus == TransactionStatus.PENDING) {
            this.groupServiceStatus = status
        }
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if (other !is MemberDeleteTransaction) return false
        if(javaClass != other?.javaClass) return false
        return this.id == other.id
    }
}