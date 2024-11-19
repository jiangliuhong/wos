package top.jiangliuhong.wos.sql

import io.xream.sqli.builder.CondBuilder
import io.xream.sqli.builder.QB
import io.xream.sqli.builder.internal.DefaultQ2Sql
import io.xream.sqli.builder.internal.SqlBuilt
import io.xream.sqli.builder.internal.SqlSubsAndValueBinding
import org.junit.jupiter.api.Test
import java.util.*


class SqlTest {

    @Test
    fun testSql() {
        val qbx = QB.x()
//        qbx.result
        qbx.select("o.id")
        qbx.eq("o.status", "PAID")
        qbx.and { sub: CondBuilder -> sub.gt("o.createAt", Date()).lt("o.createAt", Date()) }
        qbx.or { sub: CondBuilder -> sub.eq("o.test", "test").or().eq("i.test", "test") }
        qbx.from("FROM sys_user o INNER JOIN orderItem i ON i.orderId = o.id")
        qbx.paged { pb -> pb.ignoreTotalRows().page(1).rows(10).last(10000) }
        val xq = qbx.build()
        val sqlBuilder = DefaultQ2Sql.newInstance()
        val valueList =  mutableListOf<Any>()
        val sqlBuiltList =  mutableListOf<SqlBuilt>()
        val subsAndValueBinding: SqlSubsAndValueBinding = object : SqlSubsAndValueBinding {
            override fun getValueList(): List<Any> {
                return valueList
            }

            override fun getSubList(): List<SqlBuilt> {
                return sqlBuiltList
            }
        }
        val sqlBuilt = SqlBuilt()
        sqlBuilder.toSql(true, xq,sqlBuilt, subsAndValueBinding)
        println(sqlBuilt.countSql)
        println(sqlBuilt.isWith)
        println(sqlBuilt.sb.toString())
    }
}