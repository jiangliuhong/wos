package top.jiangliuhong.wos.api

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import top.jiangliuhong.wos.config.DynamicRepositoryFactory
import javax.script.Bindings
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.SimpleBindings


@RestController
@RequestMapping("/api/")
class TestApi {
    @GetMapping("test")
    fun test(): String {
        return "test"
    }

    @Autowired
//    private lateinit var dynamicRepositoryFactory: DynamicRepositoryFactory
    private lateinit var entityManager: EntityManager

    @GetMapping("test3")
    fun test3(): String {
        val domainClass = Class.forName("top.jiangliuhong.wos.dao.entity.MyTestUser")
//        val repository = dynamicRepositoryFactory.getRepository(domainClass)
//        repository.save(entity)
//        val findAll = repository.findAll()
        val criteriaBuilder: CriteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(domainClass)
        criteriaQuery.from(domainClass)
        val resultList = entityManager.createQuery(criteriaQuery).resultList
        return "test3:${resultList.size}"
    }


    @GetMapping("test2")
    fun test2(@RequestParam p: String): String {
        val scriptText = """
            var greeting = "Hello, " + name + "!"
            greeting += value
            greeting
        """.trimIndent()
        val params = mutableMapOf<String, Any?>().apply {
            put("name", "Jiangliu")
            put("value", p)
        }
        val engine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")
        val bindings: Bindings = SimpleBindings(params)
        val result = engine.eval(scriptText, bindings)
        return result?.toString() ?: "null"
    }




}

