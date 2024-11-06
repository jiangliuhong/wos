package top.jiangliuhong.wos.api

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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

