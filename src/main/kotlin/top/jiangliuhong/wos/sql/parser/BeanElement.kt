package top.jiangliuhong.wos.sql.parser

import org.h2.util.ParserUtil
import java.lang.reflect.Method


class BeanElement {

    var property: String? = null
    var setter: String? = null
    var getter: String? = null
    var clz: Class<*>? = null
    var geneType: Class<*>? = null
    var getMethod: Method? = null
    var setMethod: Method? = null

    var mapper = ""
    var length = 0
    var sqlType: String? = null
    var isJson = false

    fun initMaper() {
        mapper = ParserUtil.getMapper(property)
        mapper = ParserUtil.filterSQLKeyword(mapper)
    }

}