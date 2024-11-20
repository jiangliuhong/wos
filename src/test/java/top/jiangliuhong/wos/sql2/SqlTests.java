package top.jiangliuhong.wos.sql2;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangliuhong.wos.sql.builder.QB;
import top.jiangliuhong.wos.sql.common.Field;
import top.jiangliuhong.wos.sql.common.Table;

import java.util.ArrayList;
import java.util.List;

public class SqlTests {

    private static final Logger log = LoggerFactory.getLogger(SqlTests.class);

    @Test
    void generateSql() {
        log.info("generateSql");
        Table table = new Table();
        table.setId("order");
        table.setName("order");
        table.setSchema("db_test");
        List<Field> fields1 = new ArrayList<>();
        Field field1 = new Field();
        field1.setName("id");
        field1.setKey(true);
        fields1.add(field1);
        Field field2 = new Field();
        field2.setName("name");
        fields1.add(field2);
        table.setFields(fields1);

        QB.of("order").eq("id", "111").result("id", "name");

    }

}
