package kr.respectme.member.applications.port.command

import kr.respectme.member.applications.dto.CreateMemberCommand
import kr.respectme.member.applications.dto.LoginCommand
import kr.respectme.member.applications.dto.ModifyNicknameCommand
import kr.respectme.member.domain.dto.MemberDto
import java.util.UUID

interface MemberCommandUseCase {

    fun join(command: CreateMemberCommand): MemberDto

    fun leave(memberId: UUID, resourceId: UUID): Unit

    fun getMember(memberId: UUID, resourceId: UUID): MemberDto

    fun getMembers(memberId: UUID, memberIds: List<UUID>): List<MemberDto>

    fun deleteMemberByService(memberId: UUID)
}