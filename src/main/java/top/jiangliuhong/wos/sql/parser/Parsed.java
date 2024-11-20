package top.jiangliuhong.wos.sql.parser;

import lombok.Getter;
import lombok.Setter;
import top.jiangliuhong.wos.sql.common.Field;
import top.jiangliuhong.wos.sql.common.Table;

@Getter
@Setter
public final class Parsed {
    private Table table;
    private Field primaryKey;

    public Parsed(final Table table) {
        this.table = table;
        for (Field field : this.table.getFields()) {
            if (field.getKey()) {
                this.primaryKey = field;
                break;
            }
        }
    }

    public static Parsed of(Table table) {
        return new Parsed(table);
    }

    public String getId(){
        return this.table.getId();
    }
}
