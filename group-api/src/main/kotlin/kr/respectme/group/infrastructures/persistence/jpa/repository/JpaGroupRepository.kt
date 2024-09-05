package kr.respectme.group.infrastructures.persistence.jpa.repository

import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaNotificationGroup
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaGroupRepository: org.springframework.data.repository.Repository<JpaNotificationGroup, UUID> {

    @Modifying
    @Query(
        """
        DELETE FROM group_notification n
        WHERE n.group.id = :groupId
    """
    )
    fun deleteNotificationsByGroupId(@Param("groupId") groupId: UUID)

    @Modifying
    @Query(
        """
        DELETE FROM notification_group_member m 
        WHERE m.pk.groupId = :groupId
    """)
    fun deleteMembersByGroupId(@Param("groupId") groupId: UUID)

    fun delete(group: JpaNotificationGroup): Unit

    @Modifying
    @Query(
        """
            DELETE FROM notification_group g
            WHERE g.id = :id
        """
    )
    fun deleteById(id: UUID): Unit

    fun save(group: JpaNotificationGroup): JpaNotificationGroup

    fun findById(id: UUID): JpaNotificationGroup?

    fun findByOwnerId(ownerId: UUID): List<JpaNotificationGroup>

    @Query(
        """
        SELECT DISTINCT g 
        FROM notification_group g 
        JOIN FETCH g.members 
        WHERE g.id = :groupId
    """
    )
    fun findByIdWithMembers(groupId: UUID): JpaNotificationGroup?

    @Query(
        """
        SELECT DISTINCT g 
        FROM notification_group g 
        JOIN FETCH g.notifications 
        WHERE g.id = :groupId
    """
    )
    fun findByIdWithNotifications(groupId: UUID): JpaNotificationGroup?

    @Query(
        """
        SELECT DISTINCT g
        FROM notification_group g
        JOIN FETCH g.members m
        WHERE g.id = :groupId AND m.id IN :memberIds
    """
    )
    fun findByIdWithDesignatedMembers(groupId: UUID, memberIds: List<UUID>): JpaNotificationGroup?
}