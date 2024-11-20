package top.jiangliuhong.wos.sql.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field {
    private String name;
    private String property;
    private FieldTypes type = FieldTypes.STRING;
    private Boolean key = false;
}
