package kr.respectme.member.applications.adapter.command

import kr.respectme.common.error.ConflictException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.member.applications.dto.CreateMemberCommand
import kr.respectme.member.applications.dto.LoginCommand
import kr.respectme.member.applications.dto.ModifyNicknameCommand
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
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class MemberService(
    private val memberSavePort: MemberSavePort,
    private val memberLoadPort: MemberLoadPort,
    private val memberMapper: MemberMapper
): MemberCommandUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)


    @Transactional
    override fun join(command: CreateMemberCommand): MemberDto {

        // TODO 분산 트랜잭션이 필요함
        // 회원 가입 절차는 아래와 같다.
        // 1. [Auth Service] 회원 인증 정보 생성 및 회원 정보 생성 요청
        // 2. [Member Service] 회원 정보 생성 및 회원 객체 반환
        // 3. [Auth Service] 회원 인증 정보 생성 완료 및 JWT 토큰 발급
        // 1-3 과정이 분산 트랜잭션으로 처리되어야 한다.
        // 회원이 생성 되었는데, 인증 정보 생성이 실패할 경우 회원 정보를 삭제해야 한다.

        memberLoadPort.getMemberByEmail(command.email)?.let{
            throw ConflictException(ALREADY_EXIST_EMAIL)
        }

        val member = memberSavePort.save(
            Member(
                id = UUIDV7Generator.generate(),
                email = command.email,
                role = MemberRole.ROLE_MEMBER,
                isBlocked = false,
                blockReason = "",
                createdAt = Instant.now()
            )
        )

        return memberMapper.memberToMemberDto(member)
    }

    @Transactional
    override fun leave(memberId: UUID, targetId: UUID) {
        if(memberId != targetId) throw ForbiddenException(RESOURCE_OWNERSHIP_VIOLATION)

        memberLoadPort.getMemberById(targetId)?.let { member ->
            memberSavePort.delete(member)
        } ?: throw NotFoundException(MEMBER_NOT_FOUND)

        // TODO MemberDeleteEvent 발행 필요
        // 이벤트 발행 시 Auth Service는 이 이벤트를 수신하여 회원의 인증 정보를 삭제 해야한다.
        // Group 서비스에서는 회원이 그룹 소유자인 모든 그룹을 삭제 해야하며,
        // 그룹 멤버로 참여중인 경우 그룹에서 탈퇴 해야한다.
    }

    @Transactional(readOnly = true)
    override fun getMember(memberId: UUID, resourceId: UUID): MemberDto {
        if(memberId != resourceId) {
            throw ForbiddenException(RESOURCE_OWNERSHIP_VIOLATION)
        }

        return memberLoadPort.getMemberById(memberId)?.let{
            memberMapper.memberToMemberDto(it)
        } ?: throw NotFoundException(MEMBER_NOT_FOUND)
    }

    @Transactional(readOnly = true)
    override fun getMembers(memberId: UUID, memberIds: List<UUID>): List<MemberDto> {
        return memberLoadPort.getMembersInList(memberIds)
            .map { member -> memberMapper.memberToMemberDto(member) }
    }
}