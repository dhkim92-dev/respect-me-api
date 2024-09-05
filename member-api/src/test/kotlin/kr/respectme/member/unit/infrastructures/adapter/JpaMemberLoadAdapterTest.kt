package kr.respectme.member.unit.infrastructures.adapter

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.respectme.member.domain.mapper.MemberMapper
import kr.respectme.member.domain.model.Member
import kr.respectme.member.infrastructures.persistence.adapter.JpaMemberLoadAdapter
import kr.respectme.member.infrastructures.persistence.adapter.jpa.JpaMemberRepository
import kr.respectme.member.support.createMember
import kr.respectme.member.support.createMembers
import org.springframework.data.repository.findByIdOrNull

internal class JpaMemberLoadAdapterTest: BehaviorSpec({

    lateinit var jpaMemberRepository: JpaMemberRepository
    lateinit var jpaMemberLoadAdapter: JpaMemberLoadAdapter
    val memberMapper = MemberMapper()

    beforeSpec {
        jpaMemberRepository = mockk()
        jpaMemberLoadAdapter = JpaMemberLoadAdapter(jpaMemberRepository, memberMapper)
    }

    fun checkEqual(UUID1: Member, UUID2: Member) {
        UUID1.id shouldBe UUID2.id
        UUID1.nickname shouldBe UUID2.nickname
        UUID1.email shouldBe UUID2.email
        UUID1.role shouldBe UUID2.role
        UUID1.blockReason shouldBe UUID2.blockReason
        UUID1.isBlocked shouldBe UUID2.isBlocked
    }

    Given("A member Id") {
        val member = createMember(1)
        val jpaMember = memberMapper.toJpaEntity(member)

        When("Member exists") {
            every { jpaMemberRepository.findByIdOrNull(member.id) } returns jpaMember

            Then("Return Member") {
                val result = jpaMemberLoadAdapter.getMemberById(member.id)
                result shouldNotBe null
                checkEqual(result!!, member)
            }
        }

        When("Member does not exist") {
            every { jpaMemberRepository.findByIdOrNull(member.id) } returns null

            Then("Return null") {
                jpaMemberLoadAdapter.getMemberById(member.id) shouldBe null
            }
        }
    }

    Given("An email") {
        val member = createMember(1)
        val email = member.email

        When("If member exists with the email") {
            every { jpaMemberRepository.findByEmail(email) } returns memberMapper.toJpaEntity(member)
            Then("Return Member") {
                val result = jpaMemberLoadAdapter.getMemberByEmail(email)
                result shouldNotBe null
                checkEqual(result!!, member)
            }
        }

        When("Member does not exist") {
            every { jpaMemberRepository.findByEmail(email) } returns null
            Then("Return null") {
                jpaMemberLoadAdapter.getMemberByEmail(email) shouldBe null
            }
        }
    }

    Given("A list of UUIDs") {
        val members = createMembers(10)
        val memberIds = members.map { it.id }
        When("Members exist") {
            every { jpaMemberRepository.findAllById(memberIds) } returns members.map { memberMapper.toJpaEntity(it) }
            Then("Return Members") {
                val result = jpaMemberLoadAdapter.getMembersInList(memberIds)
                result.isEmpty() shouldBe false
                result.size shouldBe members.size
                for(i in members.indices) {
                    checkEqual(result[i], members[i])
                }
            }
        }

        When("Members do not exist") {
            every { jpaMemberRepository.findAllById(memberIds) } returns emptyList()
            Then("Return empty list") {
                jpaMemberLoadAdapter.getMembersInList(memberIds) shouldBe emptyList()
            }
        }
    }

    afterContainer{
        clearAllMocks()
    }
})