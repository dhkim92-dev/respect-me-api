package kr.respectme.member_api.applications.adapter

import kr.respectme.common.error.ConflictException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.member_api.applications.dto.CreateMemberCommand
import kr.respectme.member_api.applications.dto.ModifyNicknameCommand
import kr.respectme.member_api.applications.dto.ModifyPasswordCommand
import kr.respectme.member_api.applications.port.MemberUseCase
import kr.respectme.member_api.common.code.MemberServiceErrorCode.*
import kr.respectme.member_api.domain.dto.MemberDto
import kr.respectme.member_api.domain.mapper.MemberMapper
import kr.respectme.member_api.domain.model.Member
import kr.respectme.member_api.domain.model.MemberRole
import kr.respectme.member_api.infrastructures.port.MemberLoadPort
import kr.respectme.member_api.infrastructures.port.MemberSavePort
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class MemberService(
    private val memberSavePort: MemberSavePort,
    private val memberLoadPort: MemberLoadPort,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val memberMapper: MemberMapper
): MemberUseCase {


    override fun join(command: CreateMemberCommand): MemberDto {
        memberLoadPort.getMemberByEmail(command.email)?.let{
            throw ConflictException(ALREADY_EXIST_EMAIL)
        }

        val member = memberSavePort.save(Member(
            id = UUID.randomUUID(),
            nickname = command.nickname,
            email = command.email,
            password = passwordEncoder.encode(command.password),
            role = MemberRole.ROLE_MEMBER,
            isBlocked = false,
            blockReason = "",
            createdAt = LocalDateTime.now()
        ))

        return memberMapper.memberToMemberDto(member)
    }

    override fun changeNickname(
        memberId: UUID,
        command: ModifyNicknameCommand
    ): MemberDto {
        val targetId = command.resourceId
        if(memberId != targetId) {
            throw ForbiddenException(RESOURCE_OWNERSHIP_VIOLATION)
        }

        return memberLoadPort.getMemberById(targetId)?.let{ member->
            member.changeNickname(command.nickname)
            memberMapper.memberToMemberDto(memberSavePort.save(member))
        } ?: throw NotFoundException(MEMBER_NOT_FOUND)
    }

    override fun changePassword(memberId: UUID, command: ModifyPasswordCommand): MemberDto {
        val targetId = command.resourceId
        if(memberId != targetId) throw ForbiddenException(RESOURCE_OWNERSHIP_VIOLATION)

        return memberLoadPort.getMemberById(targetId)?.let {member ->
            member.takeIf { passwordEncoder.matches(command.password, member.password) }
                ?: throw ForbiddenException(PASSWORD_MISMATCH)
            member.changePassword(passwordEncoder, command.newPassword)
            memberMapper.memberToMemberDto(memberSavePort.save(member))
        } ?: throw NotFoundException(MEMBER_NOT_FOUND)
    }

    override fun leave(memberId: UUID, targetId: UUID) {
        if(memberId != targetId) throw ForbiddenException(RESOURCE_OWNERSHIP_VIOLATION)

        memberLoadPort.getMemberById(targetId)?.let { member ->
            memberSavePort.delete(member)
        } ?: throw NotFoundException(MEMBER_NOT_FOUND)
    }

    override fun getMember(memberId: UUID, resourceId: UUID): MemberDto {
        if(memberId != resourceId) {
            throw ForbiddenException(RESOURCE_OWNERSHIP_VIOLATION)
        }

        return memberLoadPort.getMemberById(memberId)?.let{
            memberMapper.memberToMemberDto(it)
        } ?: throw NotFoundException(MEMBER_NOT_FOUND)
    }

    override fun getMembers(memberId: UUID, memberIds: List<UUID>): List<MemberDto> {
        return memberLoadPort.getMembersInList(memberIds)
            .map { member -> memberMapper.memberToMemberDto(member) }
    }
}