package kr.respectme.member.common.saga

/**
 * 이 클래스는 이 서비스에서 시작하는 Saga 이름을 정의합니다.
 */
class SagaEventDefinition {

    companion object {
        /**
         * 멤버 삭제 Saga
         * 이 서비스에서 Listen 해야하는 추가 topics
         * member-delete-saga-auth-completed
         * member-delete-saga-group-completed
         * member-delete-saga-auth-failed
         * member-delete-saga-group-failed
         */
        const val MEMBER_DELETE_SAGA = "member-delete-saga"
        const val MEMBER_DELETE_COMPLETED_SAGA = "member-delete-completed-saga"
        const val MEMBER_DELETE_FAILED_SAGA = "member-delete-failed-saga"
    }
}