package top.jiangliuhong.wos.sql;

import org.junit.jupiter.api.Test;
import top.jiangliuhong.wos.sql.builder.Q;
import top.jiangliuhong.wos.sql.builder.QB;
import top.jiangliuhong.wos.sql.builder.internal.DefaultQ2Sql;
import top.jiangliuhong.wos.sql.builder.internal.Q2Sql;
import top.jiangliuhong.wos.sql.builder.internal.SqlBuilt;
import top.jiangliuhong.wos.sql.builder.internal.SqlSubsAndValueBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqlTests {

    @Test
    void generateSql() {
        QB.X qbx = QB.x();
        qbx.select("o.id");
        qbx.eq("o.status", "PAID");
        qbx.and(sub -> sub.gt("o.createAt", new Date()).lt("o.createAt", new Date()));
        qbx.or(sub -> sub.eq("o.test", "test").or().eq("i.test", "test"));
        qbx.from("FROM order o INNER JOIN orderItem i ON i.orderId = o.id");
        qbx.paged(pb -> pb.ignoreTotalRows().page(1).rows(10).last(10000));
        Q.X xq = qbx.build();
        Q2Sql q2Sql = DefaultQ2Sql.newInstance();
        SqlBuilt sqlBuilt = new SqlBuilt();
        List<Object> valueList = new ArrayList<>();
        List<SqlBuilt> sqlBuiltList = new ArrayList<>();
        SqlSubsAndValueBinding binding = new SqlSubsAndValueBinding() {
            @Override
            public List<Object> getValueList() {
                return valueList;
            }

            @Override
            public List<SqlBuilt> getSubList() {
                return sqlBuiltList;
            }
        };
        q2Sql.toSql(false, xq, sqlBuilt, binding);
        System.out.println(sqlBuilt.getSb().toString());
    }

}
