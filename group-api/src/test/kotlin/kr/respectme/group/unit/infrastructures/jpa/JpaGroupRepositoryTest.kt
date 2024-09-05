package kr.respectme.group.unit.infrastructures.jpa

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaGroupMember
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaNotificationGroup
import kr.respectme.group.infrastructures.persistence.jpa.repository.JpaGroupRepository
import kr.respectme.group.support.createJpaGroup
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@DataJpaTest
internal class JpaGroupRepositoryTest(
    private val groupRepository: JpaGroupRepository,
    private val groupMemberRepository: JpaGroupMemberRepository
)
: AnnotationSpec() {

    init {
        extension(SpringExtension)
    }

    var groups: MutableList<JpaNotificationGroup> = mutableListOf()
    var groupIds: MutableList<UUID> = mutableListOf()

    @BeforeEach
    fun setUp() {
        val newGroups = createJpaGroup(5)
        newGroups.forEachIndexed { index, group ->
            println(group.id)
        }

        groups = groupRepository
            .saveAll(newGroups)
            .toMutableList()
        groupIds = MutableList(groups.size){ groups[it].id }

    }

    @Test
    @Transactional
    fun`Group 생성 테스트`() {
        val group = createJpaGroup(1).first()
        val saved = groupRepository.save(group)

        println("Group Id : ${group.id}")
        println("Group Name : ${group.name}")
        println("Group Description : ${group.description}")
        println("Group Type : ${group.type}")
        println("Group Member Size : ${group.members.size}")
    }

    @Test
    @Transactional
    fun `Group 조회 테스트`() {
        val group = groupRepository.findByIdOrNull(groupIds[0])
        group?.id shouldBe  groupIds[0]
        group?.name shouldBe "group-name-${groupIds[0]}"
    }

    @Test
    @Transactional
    fun `그룹 멤버 추가`() {
        val group = groupRepository.findByIdOrNull(groupIds[0])!!
        val member = JpaGroupMember(UUID.randomUUID(), group)
        group.members.add(member)
        member.group = group
        val savedGroup = groupRepository.save(group)
        val members = groupMemberRepository.findByPkGroupId(group.id)

        savedGroup.members.size shouldBe 1
        members.size shouldBe 1
        members.forEach { it.group shouldBe savedGroup }
    }

    @AfterEach
    fun cleanUp() {
        groups.clear()
        groupIds.clear()
        groupRepository.deleteAll()
    }

}