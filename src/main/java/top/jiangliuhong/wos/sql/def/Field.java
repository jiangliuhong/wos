package top.jiangliuhong.wos.sql.def;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field {
    private String name;
    private String mapping;
    private Class<?> type;
    private Boolean key;
    private Boolean json;
}
