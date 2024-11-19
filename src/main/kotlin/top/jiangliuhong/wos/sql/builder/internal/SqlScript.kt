package top.jiangliuhong.wos.sql.builder.internal

import top.jiangliuhong.wos.sql.mapping.Script

interface SqlScript : Script {
    fun sql(): String
}