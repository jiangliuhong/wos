package top.jiangliuhong.wos.sql.consts

enum class Op(private val op: String) {
    EQ("="),
    NE("<>"),
    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    IN("IN"),
    NOT_IN("NOT IN"),
    IS_NOT_NULL("IS NOT NULL"),
    IS_NULL("IS NULL"),
    X(""),
    LIMIT("LIMIT"),
    OFFSET("OFFSET"),
    SUB("SUB"),

    NONE(""),
    AND(" AND "),
    OR(" OR "),
    ORDER_BY(" ORDER BY "),
    GROUP_BY(" GROUP BY "),
    HAVING(" HAVING "),
    WHERE(" WHERE "),
    X_AGGR("");

    fun sql(): String {
        return op
    }

    fun valueOfSql(str: String): Op {
        val t = str.trim()
        for ((index, op_) in entries.withIndex()) {
            if (op_.sql() == t) {
                return op_
            }
        }
        return NONE
    }
}