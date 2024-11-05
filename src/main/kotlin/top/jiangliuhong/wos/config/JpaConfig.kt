package top.jiangliuhong.wos.config

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.hibernate.jpa.HibernatePersistenceProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes
import org.springframework.transaction.PlatformTransactionManager
import top.jiangliuhong.wos.dao.def.DefinitionLoader
import java.util.*
import javax.sql.DataSource
import kotlin.reflect.jvm.jvmName


//@Configuration
class JpaConfig {

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var jpaProperties: JpaProperties

    @Bean
    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val factoryBean = LocalContainerEntityManagerFactoryBean()
        factoryBean.dataSource = dataSource
        factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider::class.java)
        factoryBean.setPackagesToScan("top.jiangliuhong.wos.dao.entity")
        val classList = DefinitionLoader().loadByResources()
        val managedClassNames = mutableListOf<String>()
        classList.forEach {
            managedClassNames.add(it.jvmName)
        }
        managedClassNames.add("top.jiangliuhong.wos.dao.entity.TestUser")

        val managedTypes = PersistenceManagedTypes.of(*managedClassNames.toTypedArray()) // 手动添加MyEntity类
        factoryBean.setManagedTypes(managedTypes)
        // 如果需要单独添加实体类，可以使用下面的配置
//        factoryBean.setMappingResources("META-INF/orm.xml") // 指定orm.xml位置，配置手动加载的实体
        val properties = Properties()
        properties.putAll(jpaProperties.properties)
        //properties.put("hibernate.hbm2ddl.auto", "update")
        factoryBean.setJpaProperties(properties)

        return factoryBean
    }

    @Bean
    fun entityManager(entityManagerFactory: EntityManagerFactory): EntityManager {
        return entityManagerFactory.createEntityManager()
    }

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory?): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory!!)
    }
}