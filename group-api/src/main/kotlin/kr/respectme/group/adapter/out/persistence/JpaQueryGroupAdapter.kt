package kr.respectme.group.adapter.out.persistence

import com.querydsl.core.types.NullExpression
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.respectme.group.adapter.out.persistence.entity.QJpaGroupMember
import kr.respectme.group.adapter.out.persistence.entity.QJpaNotificationGroup
import kr.respectme.group.adapter.out.persistence.entity.notifications.*
import kr.respectme.group.application.dto.group.NotificationGroupDto
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.application.dto.notification.NotificationDto
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.port.out.persistence.QueryGroupPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*


@Repository
class JpaQueryGroupAdapter(
    private val qf: JPAQueryFactory
): QueryGroupPort{

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getGroupMember(groupId: UUID, memberId: UUID): GroupMemberDto? {
        val groupMember = QJpaGroupMember.jpaGroupMember

        return qf.select(
            Projections.constructor(
                GroupMemberDto::class.java,
                groupMember.pk.groupId,
                groupMember.pk.memberId,
                groupMember.nickname,
//                groupMember.profileImageUrl,
                Expressions.nullExpression(String::class.java),
                groupMember.createdAt,
                groupMember.memberRole
            ))
            .from(groupMember)
            .where(groupMember.pk.groupId.eq(groupId).and(groupMember.pk.memberId.eq(memberId)))
            .fetchOne()
    }

    override fun getGroupMembers(groupId: UUID): List<GroupMemberDto> {
        val groupMember = QJpaGroupMember.jpaGroupMember
        return qf.select(
            Projections.constructor(
                GroupMemberDto::class.java,
                groupMember.pk.groupId,
                groupMember.pk.memberId,
                groupMember.nickname,
//                groupMember.profileImageUrl,
                Expressions.nullExpression(String::class.java),
                groupMember.createdAt,
                groupMember.memberRole
            ))
            .from(groupMember)
            .where(groupMember.pk.groupId.eq(groupId))
            .fetch()
    }


    override fun getNotification(groupId: UUID, notificationId: UUID): NotificationDto? {
        val notification = QJpaGroupNotification.jpaGroupNotification
        val jpaNotification = qf.selectFrom(notification)
            .where(notification.groupId.eq(groupId).and(notification.id.eq(notificationId)))
            .fetchOne()
        return when(jpaNotification) {
            is JpaImmediateNotification ->  NotificationDto.valueOf(jpaNotification)
            is JpaScheduledNotification -> NotificationDto.valueOf(jpaNotification)
            else -> null
        }
    }


    override fun getPublishedNotifications(groupId: UUID, cursor: UUID?, size: Int): List<NotificationDto> {
        logger.debug("getPublishedNotifications groupId: $groupId, cursor: $cursor, size: $size")

        val notification = QJpaGroupNotification.jpaGroupNotification
        val queryResult = qf.selectFrom(notification)
            .where(notification.groupId.eq(groupId)
                .and(lessOrEqualNotificationId(cursor))
            )
            .orderBy(notification.id.desc())
            .limit(size.toLong())
            .fetch()
        logger.debug("query result : ${queryResult.size}")
        val dtos = queryResult.map{
                when(it) {
                    is JpaImmediateNotification -> NotificationDto.valueOf(it)
                    is JpaScheduledNotification -> NotificationDto.valueOf(it)
                    else -> throw IllegalArgumentException("Unknown notification type")
                }
            }
        return dtos
    }

    override fun getGroup(groupId: UUID): NotificationGroupDto? {
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember
        val memberCount = qf.select(member.pk.memberId.count())
            .from(member)
            .where(member.pk.groupId.eq(groupId))
            .fetchOne()


        return qf.select(
            Projections.constructor(
                NotificationGroupDto::class.java,
                group.id,
                group.name,
                group.ownerId,
                group.description,
                group.createdAt,
                // TODO add thumbnail on JpaNotificationGroup
                Expressions.nullExpression(String::class.java),
                group.type,
                Expressions.asNumber(memberCount).intValue()
            ))
            .from(group)
            .where(group.id.eq(groupId))
            .fetchOne()
    }

    /**
     * 사용자가 가입한 그룹 목록을 조회
     * @param loginId 사용자 ID
     * @return List<NotificationGroupDto>
     */
    override fun getMemberGroups(loginId: UUID): List<NotificationGroupDto> {
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember

        val groupList = qf.select(member.pk.groupId)
            .from(member)
            .where(member.pk.memberId.eq(loginId))
            .fetch()

        return qf.select(
            Projections.constructor(
                NotificationGroupDto::class.java,
                group.id,
                group.name,
                group.ownerId,
                group.description,
                group.createdAt,
                Expressions.nullExpression(String::class.java),
                group.type,
                member.pk.memberId.count().intValue()
            ))
            .from(group)
            .join(member).on(group.id.eq(member.pk.groupId))
            .groupBy(group)
            .where(group.id.`in`(groupList))
            .fetch()
    }

    override fun getAllGroups(cursor: UUID?, size: Int?): List<NotificationGroupDto> {
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember

        return qf.select(
            Projections.constructor(
                NotificationGroupDto::class.java,
                group.id,
                group.name,
                group.ownerId,
                group.description,
                group.createdAt,
                Expressions.nullExpression(String::class.java),
                group.type,
                member.pk.memberId.count().intValue()
            ))
            .from(group)
            .join(member)
            .on(group.id.eq(member.pk.groupId))
            .where(lessOrEqualNotificationId(cursor))
            .orderBy(group.id.desc())
            .groupBy(group)
            .limit(size?.toLong() ?: 20)
            .fetch()
    }

    override fun getMemberNotifications(loginId: UUID, cursor: UUID?, size: Int): List<NotificationDto> {
        val notification = QJpaGroupNotification.jpaGroupNotification
        val immediateNotification = QJpaImmediateNotification.jpaImmediateNotification
        val scheduledNotification = QJpaScheduledNotification.jpaScheduledNotification
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val groupMember = QJpaGroupMember.jpaGroupMember
        val member = QJpaGroupMember.jpaGroupMember

        // 1. 사용자가 가입한 그룹 목록을 먼저 조회
        val groupList = qf.select(member.pk.groupId)
            .from(member)
            .where(member.pk.memberId.eq(loginId))
            .fetch()

        // 2. 사용자가 가입한 그룹의 알림 목록을 조회
        return qf.select(
            Projections.constructor(
                NotificationDto::class.java,
                notification.id,
                notification.groupId,
                notification.content,
                notification.type,
                notification.status,
                // ScheduledNotification 타입인 경우 scheduledAt을 수동으로 참조
                Expressions.cases()
                    .`when`(notification.type.eq(NotificationType.SCHEDULED))
                    .then(scheduledNotification.scheduledAt)
                    .otherwise(NullExpression(Instant::class.java)),
                Expressions.nullExpression(Int::class.java),
                Expressions.nullExpression(Int::class.java),
                notification.createdAt,
                notification.updatedAt,
                notification.lastSentAt
            ))
            .from(notification)
            .leftJoin(scheduledNotification).on(notification.id.eq(scheduledNotification.id))
            .where(notification.groupId.`in`(groupList).and(lessOrEqualNotificationId(cursor)))
            .orderBy(notification.id.desc())
            .limit(size.toLong())
            .fetch()
    }

    private fun lessOrEqualNotificationId(cursor: UUID?): BooleanExpression {
        val notification = QJpaGroupNotification.jpaGroupNotification
        return if (cursor != null) {
            notification.id.loe(cursor)
        } else {
            Expressions.TRUE
        }
    }
}