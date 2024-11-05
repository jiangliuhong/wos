package top.jiangliuhong.wos.config

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.stereotype.Component

@Component
class DynamicRepositoryFactory(
    @Autowired private val applicationContext: ApplicationContext,
    @Autowired private val entityManager: EntityManager
) {
    fun <T : Any> getRepository(domainClass: Class<T>): JpaRepository<T, Long> {
        val factory = JpaRepositoryFactory(entityManager)
        return factory.getRepository(domainClass) as JpaRepository<T, Long>
//        return factory.getRepository(domainClass, JpaRepository::class.java)
    }
}