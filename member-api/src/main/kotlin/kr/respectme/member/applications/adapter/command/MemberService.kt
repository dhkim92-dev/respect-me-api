package kr.respectme.member.applications.adapter.command

import kr.respectme.common.error.ConflictException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.member.applications.dto.CreateMemberCommand
import kr.respectme.member.applications.dto.LoginCommand
import kr.respectme.member.applications.dto.ModifyNicknameCommand
import kr.respectme.member.applications.dto.ModifyPasswordCommand
import kr.respectme.member.applications.port.command.MemberCommandUseCase
import kr.respectme.member.common.code.MemberServiceErrorCode.*
import kr.respectme.member.domain.dto.MemberDto
import kr.respectme.member.domain.mapper.MemberMapper
import kr.respectme.member.domain.model.Member
import kr.respectme.member.domain.model.MemberRole
import kr.respectme.member.infrastructures.persistence.port.command.MemberLoadPort
import kr.respectme.member.infrastructures.persistence.port.command.MemberSavePort
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class MemberService(
    private val memberSavePort: MemberSavePort,
    private val memberLoadPort: MemberLoadPort,
    private val passwordEncoder: PasswordEncoder,
    private val memberMapper: MemberMapper
): MemberCommandUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun login(command: LoginCommand): MemberDto {
        val member = memberLoadPort.getMemberByEmail(command.email)
            ?: throw NotFoundException(MEMBER_NOT_FOUND)

        if(!passwordEncoder.matches(command.password, member.password)) {
            logger.debug("password mismatching")
            throw ForbiddenException(PASSWORD_MISMATCH)
        }

        return memberMapper.memberToMemberDto(member)
    }

    override fun join(command: CreateMemberCommand): MemberDto {
        memberLoadPort.getMemberByEmail(command.email)?.let{
            throw ConflictException(ALREADY_EXIST_EMAIL)
        }

        val member = memberSavePort.save(
            Member(
                id = UUIDV7Generator.generate(),
                nickname = command.nickname,
                email = command.email,
                password = passwordEncoder.encode(command.password),
                role = MemberRole.ROLE_MEMBER,
                isBlocked = false,
                blockReason = "",
                createdAt = Instant.now()
            )
        )

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