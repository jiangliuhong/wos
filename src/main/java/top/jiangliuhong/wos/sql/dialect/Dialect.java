package top.jiangliuhong.wos.sql.dialect;

import top.jiangliuhong.wos.sql.builder.internal.DialectSupport;
import top.jiangliuhong.wos.sql.builder.internal.PageSqlSupport;
import top.jiangliuhong.wos.sql.parser.BeanElement;
import top.jiangliuhong.wos.sql.parser.Parsed;
import top.jiangliuhong.wos.sql.util.BeanUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Dialect extends DialectSupport, PageSqlSupport, ValuePost {

    String transformAlia(String mapper, Map<String, String> aliaMap, Map<String, String> resultKeyAliaMap);

    Object[] toArr(Collection<Object> list);

    Object mappingToObject(Object obj, BeanElement element);

    String createOrReplaceSql(String sql);

    String createSql(Parsed parsed, List<BeanElement> tempList);

    default String getDefaultCreateSql(Parsed parsed, List<BeanElement> tempList) {
        String space = " ";
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");

        sb.append(BeanUtil.getByFirstLower(parsed.getTableName())).append(space);
        sb.append("(");
        int size = tempList.size();
        for (int i = 0; i < size; i++) {
            String p = tempList.get(i).getProperty();

            sb.append(" ").append(p).append(" ");
            if (i < size - 1) {
                sb.append(",");
            }
        }

        sb.append(") VALUES (");

        for (int i = 0; i < size; i++) {

            sb.append("?");
            if (i < size - 1) {
                sb.append(",");
            }
        }
        sb.append(")");

        return sb.toString();
    }

}
