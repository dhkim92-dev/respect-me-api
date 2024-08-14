package kr.respectme.member.configs

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing
class JpaConfig(@PersistenceContext val em: EntityManager){
}