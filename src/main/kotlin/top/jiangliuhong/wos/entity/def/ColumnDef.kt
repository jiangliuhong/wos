package top.jiangliuhong.wos.entity.def

/**
 * 字段定义
 */
data class ColumnDef(
    val name: String,
    val type: String,
    val primaryKey: Boolean = false,
)