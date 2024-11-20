package top.jiangliuhong.wos.sql.builder.internal;

import top.jiangliuhong.wos.sql.builder.JoinType;


public class JOIN {

    private String join;
    private String alia;
    private ON on;

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getAlia() {
        return alia;
    }

    public void setAlia(String alia) {
        this.alia = alia;
    }

    public ON getOn() {
        return on;
    }

    public void setOn(ON on) {
        this.on = on;
    }

    public void setJoin(JoinType joinType) {
        this.join = joinType.sql();
    }
}
