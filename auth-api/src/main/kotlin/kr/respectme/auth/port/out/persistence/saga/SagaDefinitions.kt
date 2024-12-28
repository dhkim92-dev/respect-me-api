package kr.respectme.auth.port.out.persistence.saga

class SagaDefinitions {
    companion object {
        const val MEMBER_DELETE_SAGA_AUTH_DELETE_COMPLETED = "member-delete-saga-auth-service-completed"
        const val MEMBER_DELETE_SAGA_AUTH_DELETE_FAILED = "member-delete-saga-auth-service-failed"
    }
}