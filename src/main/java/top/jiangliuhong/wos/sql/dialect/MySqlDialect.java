package top.jiangliuhong.wos.sql.dialect;

import top.jiangliuhong.wos.sql.builder.internal.SqlScript;
import top.jiangliuhong.wos.sql.parser.BeanElement;
import top.jiangliuhong.wos.sql.parser.Parsed;
import top.jiangliuhong.wos.sql.support.TimeSupport;
import top.jiangliuhong.wos.sql.util.EnumUtil;
import top.jiangliuhong.wos.sql.util.SqlJsonUtil;
import top.jiangliuhong.wos.sql.util.SqlStringUtil;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MySqlDialect implements Dialect {
    @Override
    public String getKey() {
        return "mysql";
    }

    @Override
    public StringBuilder buildPageSql(StringBuilder sb, long start, long rows, long last) {

        if (rows == 0)
            return sb;
        sb.append(SqlScript.LIMIT).append(rows);
        if (last == 0 && start > 0) {
            sb.append(SqlScript.OFFSET).append(start);
        }
        return sb;
    }


    @Override
    public Object mappingToObject(Object obj, BeanElement element) {
        if (obj == null)
            return null;
        Class ec = element.getClz();

        if (EnumUtil.isEnum(ec)) {
            return EnumUtil.deserialize(ec, obj.toString());
        } else if (element.isJson()) {

            if (SqlStringUtil.isNullOrEmpty(obj))
                return null;
            String str = obj.toString().trim();

            if (ec == List.class) {
                Class geneType = element.getGeneType();
                return SqlJsonUtil.toList(str, geneType);
            } else if (ec == Map.class) {
                return SqlJsonUtil.toMap(str);
            } else {
                return SqlJsonUtil.toObject(str, ec);
            }
        } else if (ec == BigDecimal.class) {
            return new BigDecimal(String.valueOf(obj));
        } else if (ec == double.class || ec == Double.class) {
            return Double.valueOf(obj.toString());
        } else {
            return TimeSupport.afterReadTime(ec, obj);
        }

    }

    @Override
    public String createOrReplaceSql(String sql) {
        return sql.replaceFirst("INSERT", "REPLACE");
    }

    @Override
    public String createSql(Parsed parsed, List<BeanElement> tempList) {
        return getDefaultCreateSql(parsed, tempList);
    }

    @Override
    public Object convertJsonToPersist(Object json) {
        return json;
    }

    @Override
    public String transformAlia(String mapper, Map<String, String> aliaMap, Map<String, String> resultKeyAliaMap) {
        if (resultKeyAliaMap.containsKey(mapper)) {
            mapper = resultKeyAliaMap.get(mapper);
        }
        return mapper;
    }

    @Override
    public Object filterValue(Object object) {
        return filter(object, null);
    }

    @Override
    public Object[] toArr(Collection<Object> list) {

        if (list == null || list.isEmpty())
            return null;
        int size = list.size();
        Object[] arr = new Object[size];
        int i = 0;
        for (Object obj : list) {
            obj = filterValue(obj);
            arr[i++] = obj;
        }

        return arr;
    }


    @Override
    public String getAlterTableUpdate() {
        return SqlScript.UPDATE;
    }

    @Override
    public String getAlterTableDelete() {
        return SqlScript.DELETE_FROM;
    }

    @Override
    public String getCommandUpdate() {
        return SqlScript.SET;
    }

    @Override
    public String getCommandDelete() {
        return SqlScript.SPACE;
    }

    @Override
    public String getLimitOne() {
        return SqlScript.LIMIT_ONE;
    }

    @Override
    public String getInsertTagged() {
        return null;
    }


}
