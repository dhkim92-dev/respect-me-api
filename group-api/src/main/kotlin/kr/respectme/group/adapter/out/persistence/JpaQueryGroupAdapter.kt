package kr.respectme.group.adapter.out.persistence

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.respectme.group.adapter.out.persistence.entity.GroupNotificationQueryModel
import kr.respectme.group.adapter.out.persistence.entity.NotificationGroupQueryModel
import kr.respectme.group.adapter.out.persistence.entity.QJpaGroupMember
import kr.respectme.group.adapter.out.persistence.entity.QJpaNotificationGroup
import kr.respectme.group.adapter.out.persistence.entity.notifications.*
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.domain.GroupMemberRole
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import kr.respectme.group.port.`in`.interfaces.vo.NotificationGroupVo
import kr.respectme.group.port.`in`.interfaces.vo.Writer
import kr.respectme.group.port.out.persistence.QueryGroupPort
import org.aspectj.weaver.ast.Expr
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*


@Repository
class JpaQueryGroupAdapter(
    private val qf: JPAQueryFactory
): QueryGroupPort{

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 그룹 멤버 단건 조회
     */
    override fun getGroupMember(groupId: UUID, memberId: UUID): GroupMemberDto? {
        val groupMember = QJpaGroupMember.jpaGroupMember

        return qf.select(
            Projections.constructor(
                GroupMemberDto::class.java,
                groupMember.id,
                groupMember.groupId,
                groupMember.memberId,
                groupMember.nickname,
//                groupMember.profileImageUrl,
                Expressions.nullExpression(String::class.java),
                groupMember.createdAt,
                groupMember.memberRole
            ))
            .from(groupMember)
            .where(groupMember.groupId.eq(groupId).and(groupMember.memberId.eq(memberId)))
            .fetchOne()
    }

    /**
     * 그룹 멤버 목록 조회
     */
    override fun getGroupMembers(groupId: UUID): List<GroupMemberDto> {
        val groupMember = QJpaGroupMember.jpaGroupMember
        return qf.select(
            Projections.constructor(
                GroupMemberDto::class.java,
                groupMember.id,
                groupMember.groupId,
                groupMember.memberId,
                groupMember.nickname,
//                groupMember.profileImageUrl,
                Expressions.nullExpression(String::class.java),
                groupMember.createdAt,
                groupMember.memberRole
            ))
            .from(groupMember)
            .where(groupMember.groupId.eq(groupId))
            .fetch()
    }


    override fun getNotification(groupId: UUID, notificationId: UUID): GroupNotificationQueryModel? {
        val notification = QJpaGroupNotification.jpaGroupNotification
        val member = QJpaGroupMember.jpaGroupMember

        return selectForGroupNotificationQueryModel(2000)
            .where(notification.id.eq(notificationId))
            .fetchOne()
    }


    override fun getPublishedNotifications(groupId: UUID, cursor: UUID?, size: Int): List<GroupNotificationQueryModel> {
        logger.debug("getPublishedNotifications groupId: $groupId, cursor: $cursor, size: $size")
        val notification = QJpaGroupNotification.jpaGroupNotification
        val group = QJpaNotificationGroup.jpaNotificationGroup

        return selectForGroupNotificationQueryModel()
            .where(
                isPublishedNotification().and(
                    lessOrEqualNotificationId(cursor)
                )
            ).limit(size.toLong())
            .fetch()

    }

    override fun getGroup(loginId: UUID, groupId: UUID): NotificationGroupQueryModel? {
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember
        val owner = QJpaGroupMember("owner")
        val me = QJpaGroupMember("me")

        return qf.select(
            Projections.constructor(
                NotificationGroupQueryModel::class.java,
                group.id,
                group.type,
                group.name,
                group.description,
                Projections.constructor(
                    GroupMemberVo::class.java,
                    owner.memberId,
                    owner.nickname,
                    owner.profileImageUrl,
                    owner.createdAt,
                    owner.memberRole
                ),
                member.id.countDistinct().intValue(),
                group.createdAt,
                Expressions.nullExpression(String::class.java),
                me.memberRole.coalesce(GroupMemberRole.GUEST)
            ))
            .from(group)
            .leftJoin(member).on(member.groupId.eq(group.id))
            .leftJoin(owner).on(owner.groupId.eq(group.id).and(owner.memberRole.eq(GroupMemberRole.OWNER)))
            .leftJoin(me).on(me.groupId.eq(group.id).and(me.memberId.eq(loginId)))
            .where(group.id.eq(groupId))
            .groupBy(
                group.id,
                owner.id,
                me.memberRole,
            )
            .fetchOne()
    }
    /**
     * 사용자가 가입한 그룹 목록을 조회
     * @param loginId 사용자 ID
     * @return List<NotificationGroupDto>
     */
    override fun getMemberGroups(loginId: UUID): List<NotificationGroupQueryModel> {
        // TODO 페이지네이션 미적용 상태, 적용해야함
        logger.error("getMemberGroups loginId: $loginId")
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember
        val owner = QJpaGroupMember("owner")
        val me = QJpaGroupMember("me")

        val groupList = qf.select(member.groupId)
            .from(member)
            .where(member.memberId.eq(loginId))
            .fetch()

        return qf.select(
            Projections.constructor(
                NotificationGroupQueryModel::class.java,
                group.id,
                group.type,
                group.name,
                group.description,
                Projections.constructor(
                    GroupMemberVo::class.java,
                    owner.memberId,
                    owner.nickname,
                    owner.profileImageUrl,
                    owner.createdAt,
                    owner.memberRole
                ),
                member.id.countDistinct().intValue(),
                group.createdAt,
                Expressions.nullExpression(String::class.java),
                me.memberRole.coalesce(GroupMemberRole.GUEST)
            ))
            .from(group)
            .leftJoin(member).on(member.groupId.eq(group.id))
            .leftJoin(owner).on(owner.groupId.eq(group.id).and(owner.memberRole.eq(GroupMemberRole.OWNER)))
            .leftJoin(me).on(me.groupId.eq(group.id).and(me.memberId.eq(loginId)))
            .where(group.id.`in`(groupList))
            .groupBy(
                group.id,
                owner.id,
                me.memberRole,
            )
            .orderBy(group.id.desc())
            .fetch()
    }

    override fun getAllGroups(cursor: UUID?, size: Int?): List<NotificationGroupQueryModel> {
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember
        val owner = QJpaGroupMember("owner")
        val me = QJpaGroupMember("me")

        return qf.select(
            Projections.constructor(
                NotificationGroupQueryModel::class.java,
                group.id,
                group.type,
                group.name,
                group.description,
                Projections.constructor(
                    GroupMemberVo::class.java,
                    owner.memberId,
                    owner.nickname,
                    owner.profileImageUrl,
                    owner.createdAt,
                    owner.memberRole
                ),
                member.id.countDistinct().intValue(),
                group.createdAt,
                Expressions.nullExpression(String::class.java),
                Expressions.asEnum(GroupMemberRole.GUEST)
            ))
            .from(group)
            .leftJoin(member).on(member.groupId.eq(group.id))
            .leftJoin(owner).on(owner.groupId.eq(group.id).and(owner.memberRole.eq(GroupMemberRole.OWNER)))
            .where( lessOrEqualGroupId(cursor) )
            .limit(size?.toLong() ?: 21)
            .groupBy(
                group.id,
                owner.id,
            )
            .orderBy(group.id.desc())
            .fetch()
    }

    override fun getMemberNotifications(loginId: UUID, cursor: UUID?, size: Int): List<GroupNotificationQueryModel> {
        val notification = QJpaGroupNotification.jpaGroupNotification
        val immediateNotification = QJpaImmediateNotification.jpaImmediateNotification
        val scheduledNotification = QJpaScheduledNotification.jpaScheduledNotification
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val groupMember = QJpaGroupMember.jpaGroupMember
        val member = QJpaGroupMember.jpaGroupMember

        // 1. 사용자가 가입한 그룹 목록을 먼저 조회
        val groupList = qf.select(member.groupId)
            .from(member)
            .where(member.memberId.eq(loginId))
            .fetch()

         return selectForGroupNotificationQueryModel()
            .where(notification.groupId.`in`(groupList)
                .and(lessOrEqualNotificationId(cursor))
                .and(isPublishedNotification()))
            .orderBy(notification.id.desc())
            .limit(size.toLong())
            .fetch()
    }

    private fun lessOrEqualGroupId(cursor: UUID?): BooleanExpression {
        val group = QJpaNotificationGroup.jpaNotificationGroup
        return if (cursor != null) {
            group.id.loe(cursor)
        } else {
            Expressions.TRUE
        }
    }

    private fun lessOrEqualNotificationId(cursor: UUID?): BooleanExpression {
        val notification = QJpaGroupNotification.jpaGroupNotification
        return if (cursor != null) {
            notification.id.loe(cursor)
        } else {
            Expressions.TRUE
        }
    }

    private fun isPublishedNotification(): BooleanExpression {
        val notification = QJpaGroupNotification.jpaGroupNotification
        return notification.status.eq(NotificationStatus.PUBLISHED)
    }

    private fun selectForGroupNotificationQueryModel(contentLen: Int = 256): JPAQuery<GroupNotificationQueryModel> {
        val notification = QJpaGroupNotification.jpaGroupNotification
        val immediateNotification = QJpaImmediateNotification.jpaImmediateNotification
        val scheduledNotification = QJpaScheduledNotification.jpaScheduledNotification
        val weeklyNotification = QJpaWeeklyNotification.jpaWeeklyNotification
        val dayIntervalNotification = QJpaDayIntervalNotification.jpaDayIntervalNotification
        val member = QJpaGroupMember.jpaGroupMember
        val group = QJpaNotificationGroup.jpaNotificationGroup

        logger.debug("selectForGroupNotificationQueryModel contentLen: $contentLen")

        return qf.select(
            Projections.constructor(
                GroupNotificationQueryModel::class.java,
                notification.id,
                notification.type,
                Projections.constructor(
                    NotificationGroupVo::class.java,
                    notification.groupId,
                    group.name,
                    // TODO 그룹 이미지 URL 추가 후 수정 필요
                    Expressions.nullExpression(String::class.java)
                ),
                notification.status,
                Projections.constructor(
                    Writer::class.java,
                    notification.memberId,
                    member.nickname,
                    member.profileImageUrl
                ),
                notification.content.substring(0, contentLen),
                scheduledNotification.scheduledAt,
                weeklyNotification.dayOfWeeks,
                dayIntervalNotification.dayInterval,
                notification.createdAt,
                notification.updatedAt,
                notification.lastSentAt,
            ))
            .from(notification)
            .leftJoin(immediateNotification).on(notification.id.eq(immediateNotification.id))
            .leftJoin(scheduledNotification).on(notification.id.eq(scheduledNotification.id))
            .leftJoin(weeklyNotification).on(notification.id.eq(weeklyNotification.id))
            .leftJoin(dayIntervalNotification).on(notification.id.eq(dayIntervalNotification.id))
            .leftJoin(member).on(notification.groupId.eq(member.groupId).and(notification.memberId.eq(member.memberId)))
            .leftJoin(group).on(notification.groupId.eq(group.id))
            .distinct()
            .fetchJoin()
    }
}