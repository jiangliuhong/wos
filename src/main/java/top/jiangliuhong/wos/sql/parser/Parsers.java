package top.jiangliuhong.wos.sql.parser;

import top.jiangliuhong.wos.sql.common.Table;

import java.util.concurrent.ConcurrentHashMap;

public final class Parsers {

    private static ConcurrentHashMap<String, Parsed> map = new ConcurrentHashMap<>();

    public static void register(Table table) {
        Parsed parsed = Parsed.of(table);
        map.put(parsed.getId(), parsed);
    }

    public static Parsed get(String id) {
        return map.get(id);
    }


}
