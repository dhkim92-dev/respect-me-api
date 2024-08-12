package kr.respectme.member_api.interfaces.adapter

import jakarta.validation.Valid
import kr.respectme.member_api.applications.dto.CreateMemberCommand
import kr.respectme.member_api.applications.dto.ModifyNicknameCommand
import kr.respectme.member_api.applications.dto.ModifyPasswordCommand
import kr.respectme.member_api.applications.port.MemberUseCase
import kr.respectme.member_api.domain.model.Member
import kr.respectme.member_api.interfaces.dto.*
import kr.respectme.member_api.interfaces.port.MemberCommandPort
import kr.respectme.member_api.interfaces.port.MemberQueryPort
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("api/v1/members")
class RestAdapter(
    private val memberUseCase: MemberUseCase
): MemberQueryPort, MemberCommandPort {

    @PostMapping
    override fun createMember(@RequestBody @Valid request: CreateMemberRequest)
    : MemberResponse {
        return MemberResponse.of(memberUseCase.join(CreateMemberCommand.of(request)))
    }

    @PatchMapping("/{resourceId}/nickname")
    override fun updateNickname(
        loginMember: Member,
        @PathVariable resourceId: UUID,
        @RequestBody @Valid request: ModifyMemberRequest)
    : MemberResponse {
        return MemberResponse.of(memberUseCase.changeNickname(loginMember.id, ModifyNicknameCommand.of(resourceId, request)))
    }

    @PatchMapping("/{resourceId}/password")
    override fun updatePassword(
        loginMember: Member,
        @PathVariable resourceId: UUID,
        @RequestBody @Valid request: ModifyMemberRequest)
    : MemberResponse {
        return MemberResponse.of(memberUseCase.changePassword(loginMember.id, ModifyPasswordCommand.of(resourceId, request)))
    }

    @DeleteMapping("/{resourceId}")
    override fun deleteMember(loginMember: Member, @PathVariable resourceId: UUID) {
        return memberUseCase.leave(loginMember.id, resourceId)
    }

    @GetMapping("/{resourceId}")
    override fun getMember(loginMember: Member, @PathVariable resourceId: UUID): MemberResponse {
        return MemberResponse.of(memberUseCase.getMember(loginMember.id, resourceId))
    }

    override fun getMembers(loginMember: Member, @RequestBody @Valid request: QueryMembersRequest): List<MemberResponse> {
        return memberUseCase.getMembers(loginMember.id, request.memberIds)
            .map { memberDto -> MemberResponse.of(memberDto)}
    }
}