package kr.respectme.member.port.out.event

enum class TransactionStatus(val statusName: String, val value: Int) {
    PENDING(statusName = "PENDING", value = 1),
    FAILED(statusName = "FAILED", value = 2),
    COMPLETED(statusName = "COMPLETED", value = 3),
}
