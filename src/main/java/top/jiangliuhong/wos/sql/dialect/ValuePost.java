package top.jiangliuhong.wos.sql.dialect;

import top.jiangliuhong.wos.sql.util.EnumUtil;

import java.util.Objects;

public interface ValuePost {

    default Object filter(Object object, MoreFilter moreFilter) {
        Object o = null;
        if (object instanceof String) {
            String str = (String) object;
            o = str.replace("<", "&lt").replace(">", "&gt");
        } else if (Objects.nonNull(object) && EnumUtil.isEnum(object.getClass())) {
            o = EnumUtil.serialize((Enum) object);
        } else {
            o = object;
        }

        if (moreFilter == null)
            return o;

        return moreFilter.filter(o);
    }

    interface MoreFilter {
        Object filter(Object object);
    }
}
