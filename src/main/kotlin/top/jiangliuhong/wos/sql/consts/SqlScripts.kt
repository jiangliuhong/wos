package top.jiangliuhong.wos.sql.consts


object SqlScripts {

    val SOURCE_SCRIPT: List<String> = mutableListOf(
        "INNER",
        "LEFT",
        "RIGHT",
        "OUTER",
        "JOIN",
        ",",
        "FULL",
        "ON",
        "AND",
        "OR",
        "LIKE",
        "!=",
        "<=",
        ">=",
        "<>",
        "=",
        "<",
        ">",
        "(",
        ")"
    )

    var KEYWORDS: Array<String> = arrayOf(
        "order",
        "state",
        "desc",
        "group",
        "asc",
        "key",
        "select",
        "delete",
        "from",
        "update",
        "create",
        "drop",
        "dump",
        "alter",
        "all",
        "distinct",
        "table",
        "column",
        "database",
        "left",
        "right",
        "inner",
        "join",
        "union",
        "natural",
        "between",
        "except",
        "in",
        "as",
        "into",
        "set",
        "values",
        "min",
        "max",
        "sum",
        "avg",
        "count",
        "on",
        "where",
        "and",
        "add",
        "index",
        "exists",
        "or",
        "null",
        "is",
        "not",
        "by",
        "having",
        "concat",
        "cast",
        "convert",
        "case",
        "when",
        "like",
        "replace",
        "primary",
        "foreign",
        "references",
        "char",
        "varchar",
        "varchar2",
        "int",
        "bigint",
        "smallint",
        "tinyint",
        "text",
        "longtext",
        "tinytext",
        "decimal",
        "numeric",
        "float",
        "double",
        "timestamp",
        "date",
        "real",
        "precision",
        "date",
        "datetime",
        "boolean",
        "bool",
        "blob",
        "now",
        "function",
        "procedure",
        "trigger"
    )

    const val ALTER_TABLE: String = "ALTER TABLE"
    const val SELECT: String = "SELECT"
    const val DISTINCT: String = "DISTINCT"
    const val WHERE: String = " WHERE "
    const val FROM: String = "FROM"
    const val LIMIT: String = " LIMIT "
    const val OFFSET: String = " OFFSET "
    const val SET: String = "SET"
    const val UPDATE: String = "UPDATE"
    const val DELETE: String = " DELETE "
    const val DELETE_FROM: String = "DELETE FROM"
    const val IN: String = " IN "
    const val ON: String = " ON "

    const val AS: String = " AS "

    const val PLACE_HOLDER: String = "?"
    const val EQ_PLACE_HOLDER: String = " = ?"
    const val LIKE_HOLDER: String = "%"
    const val COMMA: String = ","
    const val STAR: String = "*"
    const val UNDER_LINE: String = "_"
    const val DOLLOR: String = "$"
    const val SINGLE_QUOTES: String = "'"
    const val KEYWORD_MARK: String = "`"
    const val SUB: String = "\${SUB}"
    const val WITH_PLACE: String = "\${WP}"
    const val LIMIT_ONE: String = " LIMIT 1"

    const val CREATE_TEMPORARY_TABLE: String = "CREATE TEMPORARY TABLE IF NOT EXISTS "
}