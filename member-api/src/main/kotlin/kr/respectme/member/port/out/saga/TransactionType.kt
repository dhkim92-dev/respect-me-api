package kr.respectme.member.port.out.saga

enum class TransactionType(val typeName: String, val value: Int) {
    CREATE(typeName = "CREATE", value = 1),
    UPDATE(typeName = "UPDATE", value = 2),
    DELETE(typeName = "DELETE", value = 3)
}