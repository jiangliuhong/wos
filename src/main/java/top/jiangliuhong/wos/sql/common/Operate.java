package top.jiangliuhong.wos.sql.common;

public enum Operate {

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

    private final String operate;

    Operate(String str) {
        operate = str;
    }

    public String sql() {
        return operate;
    }

    public static Operate valueOfSql(String str) {
        String t = str.trim();
        for (Operate op : values()) {
            if (op.sql().equals(str))
                return op;
        }
        return NONE;
    }
}
