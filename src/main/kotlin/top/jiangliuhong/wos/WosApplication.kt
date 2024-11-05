package top.jiangliuhong.wos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import top.jiangliuhong.wos.dao.def.DefinitionLoader

@SpringBootApplication
//@EntityScan("top.jiangliuhong.wos.dao.entity")
//@EnableJpaRepositories("top.jiangliuhong.wos.dao.repository")
class WosApplication

fun main(args: Array<String>) {
	val loader = DefinitionLoader()
	val dynamicEntityClasses = loader.loadByResources()
    runApplication<WosApplication>(*args)
}
