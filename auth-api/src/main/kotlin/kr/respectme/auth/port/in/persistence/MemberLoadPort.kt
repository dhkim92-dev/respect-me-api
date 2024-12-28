package kr.respectme.auth.port.`in`.persistence

import kr.respectme.auth.port.`in`.msa.members.dto.Member
import kr.respectme.common.response.ApiResult
import java.util.UUID

interface MemberLoadPort {

//    fun loadMemberByEmailAndPassword(loginRequest: LoginRequest): ApiResult<Member?>

    fun loadMemberById(id: UUID): ApiResult<Member?>
}