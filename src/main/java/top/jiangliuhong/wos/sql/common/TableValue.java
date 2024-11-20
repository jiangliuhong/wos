package top.jiangliuhong.wos.sql.common;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class TableValue extends HashMap<String, Object> {

    private final Table table;

    public TableValue(Table table) {
        this.table = table;
    }
}
