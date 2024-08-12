package kr.respectme.member_api.infrastructures.adapter

import kr.respectme.member_api.infrastructures.adapter.jpa.JpaMemberRepository
import kr.respectme.member_api.domain.mapper.MemberMapper
import kr.respectme.member_api.domain.model.Member
import kr.respectme.member_api.infrastructures.port.MemberLoadPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class JpaMemberLoadAdapter(
    private val jpaMemberRepository: JpaMemberRepository,
    private val memberMapper: MemberMapper,
): MemberLoadPort {


    override fun getMemberById(id: UUID): Member? {
        return jpaMemberRepository.findByIdOrNull(id)
            ?.let {
                memberMapper.toDomainEntity(it)
            }
    }

    override fun getMemberByEmail(email: String): Member? {
        return jpaMemberRepository.findByEmail(email)
            ?.let {
                memberMapper.toDomainEntity(it)
            }
    }

    override fun getMembersInList(memberIds: List<UUID>): List<Member> {
        return jpaMemberRepository.findAllById(memberIds)
            .map{ memberMapper.toDomainEntity(it) }
    }
}
