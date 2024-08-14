package kr.respectme.auth.infrastructures.ports

import kr.respectme.auth.infrastructures.dto.LoginRequest
import kr.respectme.auth.infrastructures.dto.Member
import kr.respectme.common.response.ApiResult
import java.util.UUID

interface MemberLoadPort {

    fun getMemberByEmailAndPassword(loginRequest: LoginRequest): ApiResult<Member?>

    fun getMemberById(id: UUID): ApiResult<Member?>
}