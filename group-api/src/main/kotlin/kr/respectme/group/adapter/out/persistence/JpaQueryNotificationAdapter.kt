package kr.respectme.group.adapter.out.persistence

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.respectme.group.adapter.out.persistence.entity.GroupNotificationQueryModel
import kr.respectme.group.adapter.out.persistence.entity.notifications.QJpaGroupNotification
import kr.respectme.group.port.out.persistence.QueryNotificationPort
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class JpaQueryNotificationAdapter(
    private val qf: JPAQueryFactory
): QueryNotificationPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun findNotificationById(notificationId: UUID): GroupNotificationQueryModel? {
        return null
    }

    override fun findByNotificationsByGroupId(
        groupId: UUID,
        cursorId: UUID,
        pageable: Pageable
    ): Slice<GroupNotificationQueryModel> {

        return SliceImpl(
            emptyList(),
            pageable,
            false
        )
    }

    override fun findTodayNotificationCount(groupId: UUID): Int {
        val notification = QJpaGroupNotification.jpaGroupNotification
        // 쿼리 일시를 기준으로 YYYY-MM-DD 00:00:00 ~ 23:59:59 사이의 데이터를 조회
        val today = Instant.now()

        return qf.select(notification.count())
            .from(notification)
            .where(notification.groupId.eq(groupId)
                .and(notification.createdAt.between(
                    today.truncatedTo(java.time.temporal.ChronoUnit.DAYS),
                    today.plusSeconds(86399)
                ))
            )
            .fetchOne()
            ?.toInt()
            ?: 0
    }
}