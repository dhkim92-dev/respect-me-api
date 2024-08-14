package kr.respectme.member.unit.applications

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.respectme.common.error.ConflictException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.member.applications.adapter.MemberService
import kr.respectme.member.applications.dto.CreateMemberCommand
import kr.respectme.member.applications.dto.ModifyNicknameCommand
import kr.respectme.member.common.code.MemberServiceErrorCode
import kr.respectme.member.common.code.MemberServiceErrorCode.RESOURCE_OWNERSHIP_VIOLATION
import kr.respectme.member.domain.mapper.MemberMapper
import kr.respectme.member.domain.model.MemberRole
import kr.respectme.member.infrastructures.port.MemberLoadPort
import kr.respectme.member.infrastructures.port.MemberSavePort
import kr.respectme.member.support.createMember
import kr.respectme.member.support.createMembers
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

internal class MemberServiceTest: BehaviorSpec({

    lateinit var memberSavePort: MemberSavePort
    lateinit var memberLoadPort: MemberLoadPort
    val passwordEncoder = BCryptPasswordEncoder(10)
    val memberMapper = MemberMapper()
    lateinit var memberService: MemberService

    beforeContainer {
        memberSavePort = mockk()
        memberLoadPort = mockk()
        memberService = MemberService(
            memberSavePort,
            memberLoadPort,
            passwordEncoder,
            memberMapper
        )
    }

    Given("CreateMemberCommand") {
        val member = createMember(1)
        val command = CreateMemberCommand(
            nickname = member.nickname,
            email = member.email,
            password = "test1234",
        )

        When("Email is already taken") {
            every { memberLoadPort.getMemberByEmail(any()) } returns member

            Then("Throw ConflictException") {
                shouldThrow<ConflictException> {
                    memberService.join(command)
                }.message shouldBe MemberServiceErrorCode.ALREADY_EXIST_EMAIL.message
            }
        }

        When("Email is not exist") {
            every { memberLoadPort.getMemberByEmail(any()) } returns null
            every { memberSavePort.save(any()) } returns member

            Then("Sign up success") {
                val result = memberService.join(command)

                result.nickname shouldBe command.nickname
                result.email shouldBe command.email
                result.role shouldBe MemberRole.ROLE_MEMBER
                result.isBlocked shouldBe false
                result.blockReason shouldBe ""
            }
        }
    }

    Given("MemberID and ModifyMemberCommand") {
        val member = createMember(1)
        val command = ModifyNicknameCommand(resourceId = member.id, nickname = "new-nickname")

        When("Resource member not exist") {
            every { memberLoadPort.getMemberById(any()) } returns null

            Then("Throw NotFoundException") {
                shouldThrow<NotFoundException> {
                    memberService.changeNickname(member.id, command)
                }.message shouldBe MemberServiceErrorCode.MEMBER_NOT_FOUND.message
            }
        }

        When("Request member is not owner of resource") {
            val loginMemberId = UUID.randomUUID()
            Then("Throw ForbiddenException") {
                shouldThrow<ForbiddenException> {
                    memberService.changeNickname(loginMemberId, command)
                }.message shouldBe RESOURCE_OWNERSHIP_VIOLATION.message
            }
        }

        When("Resource owner's request and resource exists") {
            every { memberLoadPort.getMemberById(any()) } returns member
            every { memberSavePort.save(any()) } returns member

            Then("Member modified") {
                val result = memberService.changeNickname(member.id, command)
                result.id shouldBe member.id
                result.email shouldBe member.email
                result.nickname shouldBe command.nickname
                result.role shouldBe member.role
                result.blockReason shouldBe member.blockReason
            }
        }
    }

    Given("Login Member and Resource Member") {
        val login = createMember(1)
        val resource = createMember(1)

        When("Login member try to delete Resource member") {
            Then("occur ForbiddenException") {
                shouldThrow<ForbiddenException> {
                    memberService.leave(login.id, resource.id)
                }.message shouldBe RESOURCE_OWNERSHIP_VIOLATION.message
            }
        }

        When("Resource member try to delete own account") {
            every { memberLoadPort.getMemberById(any()) } returns resource
            every { memberSavePort.delete(any()) } returns Unit
            Then("success") {
                shouldNotThrowAny {
                    memberService.leave(resource.id, resource.id)
                }
            }
        }

        When("Login member try to get own resource") {
            every { memberLoadPort.getMemberById(login.id) } returns login
            Then("success") {
                val result = memberService.getMember(login.id, login.id)
                result.id shouldBe login.id
                result.email shouldBe login.email
                result.nickname shouldBe login.nickname
                result.role shouldBe login.role
                result.blockReason shouldBe login.blockReason
            }
        }

        When("Login member try to get other member's resource") {
            every { memberLoadPort.getMemberById(resource.id) } returns resource
            Then("occur ForbiddenException") {
                shouldThrow<ForbiddenException> {
                    memberService.getMember(login.id, resource.id)
                }.message shouldBe RESOURCE_OWNERSHIP_VIOLATION.message
            }
        }
    }

    Given("List of Member Ids") {
        val members = createMembers(3)
        val memberIds = members.map { it.id }

        When("Get members by list of member ids") {
            every { memberLoadPort.getMembersInList(any()) } returns members

            Then("success") {
                val result = memberService.getMembers(members[0].id, memberIds)
                result.size shouldBe members.size
                result.forEachIndexed { index, memberDto ->
                    memberDto.id shouldBe members[index].id
                    memberDto.email shouldBe members[index].email
                    memberDto.nickname shouldBe members[index].nickname
                    memberDto.role shouldBe members[index].role
                    memberDto.blockReason shouldBe members[index].blockReason
                }
            }
        }
    }

    afterContainer {
        clearAllMocks()
    }
})