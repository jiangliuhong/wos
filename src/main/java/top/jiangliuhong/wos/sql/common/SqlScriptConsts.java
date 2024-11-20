package top.jiangliuhong.wos.sql.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SqlScriptConsts {

    String SPACE = " ";
    String DOT = ".";
    String LEFT_PARENTTHESIS = "(";
    String RIGHT_PARENTTHESIS = ")";

    List<String> SOURCE_SCRIPT = Arrays.asList("INNER", "LEFT", "RIGHT", "OUTER", "JOIN", ",", "FULL", "ON", "AND", "OR", "LIKE", "!=", "<=", ">=", "<>", "=", "<", ">", "(", ")");

    Set<String> OPERATES = new HashSet<>(){
        {
            add("=");
            add("!");
            add(">");
            add("<");
            add("+");
            add("-");
            add("*");
            add("/");
            add(";");
            add(":");
        }
    };
    String[] KEYWORDS = {
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
    };

    String ALTER_TABLE = "ALTER TABLE";
    String SELECT = "SELECT";
    String DISTINCT = "DISTINCT";
    String WHERE = " WHERE ";
    String FROM = "FROM";
    String LIMIT = " LIMIT ";
    String OFFSET = " OFFSET ";
    String SET = "SET";
    String UPDATE = "UPDATE";
    String DELETE = " DELETE ";
    String DELETE_FROM = "DELETE FROM";
    String IN = " IN ";
    String ON = " ON ";

    String AS = " AS ";

    String PLACE_HOLDER = "?";
    String EQ_PLACE_HOLDER = " = ?";
    String LIKE_HOLDER = "%";
    String COMMA = ",";
    String STAR = "*";
    String UNDER_LINE = "_";
    String DOLLOR = "$";
    String SINGLE_QUOTES = "'";
    String KEYWORD_MARK = "`";
    String SUB = "${SUB}";
    String WITH_PLACE = "${WP}";
    String LIMIT_ONE = " LIMIT 1";

    String CREATE_TEMPORARY_TABLE = "CREATE TEMPORARY TABLE IF NOT EXISTS ";
}
