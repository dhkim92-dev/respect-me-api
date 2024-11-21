package kr.respectme.group.infrastructures.persistence.jpa.adapter

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.respectme.group.application.dto.group.NotificationGroupDto
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.application.dto.notification.NotificationDto
import kr.respectme.group.domain.GroupType
import kr.respectme.group.domain.mapper.NotificationMapper
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.infrastructures.persistence.jpa.entity.QJpaGroupMember
import kr.respectme.group.infrastructures.persistence.jpa.entity.QJpaNotificationGroup
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.JpaImmediateNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.JpaScheduledNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.QJpaGroupNotification
import kr.respectme.group.infrastructures.persistence.port.QueryGroupPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
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

    private fun gtCursor(cursor: UUID?) = QJpaGroupNotification.jpaGroupNotification.id.gt(cursor)
}