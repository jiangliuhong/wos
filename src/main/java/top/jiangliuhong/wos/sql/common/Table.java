package top.jiangliuhong.wos.sql.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Table {
    /**
     * 数据表唯一标识，与数据库定义无关
     */
    private String id;
    private String schema;
    private String name;
    private List<Field> fields;
}
