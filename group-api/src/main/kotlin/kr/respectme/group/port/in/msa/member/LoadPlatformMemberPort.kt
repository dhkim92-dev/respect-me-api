package kr.respectme.group.port.`in`.msa.member

import kr.respectme.group.port.`in`.msa.member.dto.PlatformMember
import java.util.UUID

interface LoadPlatformMemberPort {

    fun getMember(memberId: UUID): PlatformMember
}