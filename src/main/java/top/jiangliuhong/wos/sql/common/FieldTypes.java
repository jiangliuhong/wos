package top.jiangliuhong.wos.sql.common;

import lombok.Getter;
import top.jiangliuhong.wos.sql.common.type.*;

@Getter
public enum FieldTypes {
    INT(ValueInt.class),
    DATE(ValueDate.class),
    LONG(ValueLong.class),
    JSON(ValueJSON.class),
    STRING(ValueString.class),
    ;

    private final Class<? extends ValueType> typeClass;

    FieldTypes(Class<? extends ValueType> typeClass) {
        this.typeClass = typeClass;
    }
}
