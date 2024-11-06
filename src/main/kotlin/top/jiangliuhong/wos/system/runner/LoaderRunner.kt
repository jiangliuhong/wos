package top.jiangliuhong.wos.system.runner

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import top.jiangliuhong.wos.entity.EntityLoader

@Component
class LoaderRunner : ApplicationRunner {

    val log: Logger = LoggerFactory.getLogger(LoaderRunner::class.java)

    override fun run(args: ApplicationArguments?) {
        val entityDefList = EntityLoader().loadAllResourceDef()
        log.info("load entity def : ${entityDefList.size}")
    }
}