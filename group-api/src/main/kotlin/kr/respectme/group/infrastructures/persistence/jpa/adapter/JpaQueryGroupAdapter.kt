package kr.respectme.group.infrastructures.persistence.jpa.adapter

import com.querydsl.core.types.NullExpression
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.respectme.group.application.dto.group.NotificationGroupDto
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.application.dto.notification.NotificationDto
import kr.respectme.group.domain.GroupType
import kr.respectme.group.domain.mapper.NotificationMapper
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.infrastructures.persistence.jpa.entity.QJpaGroupMember
import kr.respectme.group.infrastructures.persistence.jpa.entity.QJpaNotificationGroup
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.JpaImmediateNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.JpaScheduledNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.QJpaGroupNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.QJpaImmediateNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.QJpaScheduledNotification
import kr.respectme.group.infrastructures.persistence.port.QueryGroupPort
import org.aspectj.weaver.ast.Expr
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class JpaQueryGroupAdapter(
    private val qf: JPAQueryFactory
): QueryGroupPort {

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
            .where(notification.member.pk.groupId.eq(groupId).and(notification.id.eq(notificationId)))
            .fetchOne()
        return when(jpaNotification) {
            is JpaImmediateNotification -> NotificationMapper.mapToNotificationDto(jpaNotification)
            is JpaScheduledNotification -> NotificationMapper.mapToNotificationDto(jpaNotification)
            else -> null
        }
    }


    override fun getPublishedNotifications(groupId: UUID, cursor: UUID?, size: Int): List<NotificationDto> {
        logger.debug("getPublishedNotifications groupId: $groupId, cursor: $cursor, size: $size")

        val notification = QJpaGroupNotification.jpaGroupNotification
        val queryResult = qf.selectFrom(notification)
            .where(notification.member.pk.groupId.eq(groupId))
//                .and(gtCursor(cursor))
//                .and(notification.status.eq(NotificationStatus.PUBLISHED)))
            .orderBy(notification.id.desc())
            .limit(size.toLong())
            .fetch()
        logger.debug("query result : ${queryResult.size}")
        val dtos = queryResult.map{
                when(it) {
                    is JpaImmediateNotification -> NotificationMapper.mapToNotificationDto(it)
                    is JpaScheduledNotification -> NotificationMapper.mapToNotificationDto(it)
                    else -> throw IllegalArgumentException("Unknown notification type")
                }
            }
        return dtos
    }

    override fun getGroup(groupId: UUID): NotificationGroupDto? {
        val group = QJpaNotificationGroup.jpaNotificationGroup
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
                group.members.size().intValue()
            ))
            .from(group)
            .where(group.id.eq(groupId))
            .fetchOne()
    }

    override fun getMemberGroups(loginId: UUID): List<NotificationGroupDto> {
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
                group.members.size().intValue()
            ))
            .from(member)
            .leftJoin(member.group, group)
            .where(member.pk.memberId.eq(loginId))
            .distinct()
            .orderBy(member.pk.groupId.desc())
//            .orderBy(group.createdAt.desc())
            .fetch()
    }

    override fun getAllGroups(cursor: UUID?, size: Int?): List<NotificationGroupDto> {
        val group = QJpaNotificationGroup.jpaNotificationGroup

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
                group.members.size().intValue()
            ))
            .from(group)
            .orderBy(group.id.desc())
            .limit(size?.toLong() ?: 20)
            .fetch()
    }

    override fun getMemberNotifications(loginId: UUID, cursor: UUID?, size: Int): List<NotificationDto> {
        val notification = QJpaGroupNotification.jpaGroupNotification
        val immediateNotification = QJpaImmediateNotification.jpaImmediateNotification
        val scheduledNotification = QJpaScheduledNotification.jpaScheduledNotification
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val groupMember = QJpaGroupMember.jpaGroupMember

        // 1. 사용자가 가입한 그룹 목록을 먼저 조회
        val groupList = qf.select(group.id)
            .from(group)
            .join(group.members, groupMember)
            .where(QJpaGroupMember.jpaGroupMember.pk.memberId.eq(loginId))
            .fetch()

        // 2. 사용자가 가입한 그룹의 알림 목록을 조회
        return qf.select(
            Projections.constructor(
                NotificationDto::class.java,
                notification.id,
                notification.member.pk.groupId,
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
            .where(notification.group.id.`in`(groupList))
            .orderBy(notification.id.desc())
            .limit(size.toLong())
            .fetch()
    }

    private fun gtCursor(cursor: UUID?) = QJpaGroupNotification.jpaGroupNotification.id.gt(cursor)
}