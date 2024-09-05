package kr.respectme.group.infrastructures.msa.member.port

import kr.respectme.group.infrastructures.msa.member.dto.PlatformMember
import java.util.UUID

interface LoadPlatformMemberPort {

    fun getMember(memberId: UUID): PlatformMember
}