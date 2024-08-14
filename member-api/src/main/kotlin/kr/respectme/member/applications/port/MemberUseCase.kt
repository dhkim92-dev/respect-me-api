package kr.respectme.member.applications.port

import kr.respectme.member.applications.dto.CreateMemberCommand
import kr.respectme.member.applications.dto.LoginCommand
import kr.respectme.member.applications.dto.ModifyNicknameCommand
import kr.respectme.member.applications.dto.ModifyPasswordCommand
import kr.respectme.member.domain.dto.MemberDto
import java.util.UUID

interface MemberUseCase {

    fun login(command: LoginCommand): MemberDto

    fun join(command: CreateMemberCommand): MemberDto

    fun changeNickname(memberId: UUID, command: ModifyNicknameCommand): MemberDto

    fun changePassword(memberId: UUID, command: ModifyPasswordCommand): MemberDto

    fun leave(memberId: UUID, resourceId: UUID): Unit

    fun getMember(memberId: UUID, resourceId: UUID): MemberDto

    fun getMembers(memberId: UUID, memberIds: List<UUID>): List<MemberDto>
}