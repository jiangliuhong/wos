package top.jiangliuhong.wos.sql.builder;

import lombok.Getter;

import java.util.List;

@Getter
public class QB {

    public static QB of(String tableId) {
        return new QB(tableId);
    }

    private final String tableId;

    public QB(String tableId) {
        this.tableId = tableId;
    }

    public QB eq(String column, Object value) {
        // TODO
        return this;
    }

    public QB ne(String column, Object value) {
        // TODO
        return this;
    }

    public QB gt(String column, Object value) {
        // TODO
        return this;
    }

    public QB gte(String column, Object value) {
        // TODO
        return this;
    }

    public QB lt(String column, Object value) {
        // TODO
        return this;
    }

    public QB lte(String column, Object value) {
        // TODO
        return this;
    }

    public QB like(String column, Object value) {
        // TODO
        return this;
    }

    public QB notLike(String column, Object value) {
        // TODO
        return this;
    }

    public QB in(String column, List<?> values) {
        return in(column, values.toArray());
    }

    public QB in(String column, Object... values) {
        // TODO
        return this;
    }

    public QB notIn(String column, List<?> values) {
        return notIn(column, values.toArray());
    }

    public QB notIn(String column, Object... values) {
        // TODO
        return this;
    }

    public QB isNull(String column) {
        // TODO
        return this;
    }

    public QB isNotNull(String column) {
        // TODO
        return this;
    }

    public QB result(String ... column){
        // TODO
        return this;
    }
}
