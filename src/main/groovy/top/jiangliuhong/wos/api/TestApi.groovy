package top.jiangliuhong.wos.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestApi {
    @GetMapping("test")
    String test() {
        "test1"
    }
}
