package top.jiangliuhong.wos.sql.def;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Table {
    private String schema;
    private String name;
    private String mapping;
    private List<Field> fields;

    public String getFullName() {
        return schema + "." + name;
    }
}
