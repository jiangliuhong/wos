package top.jiangliuhong.wos.entity.def

/**
 * 实体定义
 */
data class EntityDef(
    val tableName: String,
    val columns: List<ColumnDef>,
)