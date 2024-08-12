package kr.respectme.member_api.applications.port

import kr.respectme.member_api.applications.dto.CreateMemberCommand
import kr.respectme.member_api.applications.dto.ModifyNicknameCommand
import kr.respectme.member_api.applications.dto.ModifyPasswordCommand
import kr.respectme.member_api.domain.dto.MemberDto
import java.util.UUID

interface MemberUseCase {

    fun join(command: CreateMemberCommand): MemberDto

    fun changeNickname(memberId: UUID, command: ModifyNicknameCommand): MemberDto

    fun changePassword(memberId: UUID, command: ModifyPasswordCommand): MemberDto

    fun leave(memberId: UUID, resourceId: UUID): Unit

    fun getMember(memberId: UUID, resourceId: UUID): MemberDto

    fun getMembers(memberId: UUID, memberIds: List<UUID>): List<MemberDto>
}