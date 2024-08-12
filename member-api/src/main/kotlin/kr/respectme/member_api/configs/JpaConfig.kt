package kr.respectme.member_api.configs

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import javax.sql.DataSource

@Configuration
@EnableJpaAuditing
class JpaConfig(@PersistenceContext val em: EntityManager){
}