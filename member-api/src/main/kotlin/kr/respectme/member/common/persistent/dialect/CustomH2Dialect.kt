package kr.respectme.member.common.persistent.dialect

import org.hibernate.dialect.PostgreSQLDialect

class CustomH2PostgresModeDialect : PostgreSQLDialect() {
    override fun getForUpdateString(): String {
        // PostgreSQL의 FOR NO KEY UPDATE를 H2에서 지원되는 FOR UPDATE로 매핑
        return "FOR UPDATE"
    }
}