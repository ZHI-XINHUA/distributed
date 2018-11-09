package pullpush;

import java.io.Serializable;

/**
 * WorkServer 服务器配置信息
 */
public class ServerConfig implements Serializable {

    private static final long serialVersionUID = -8553491178420201441L;
    /**数据库连接地址**/
    private String dbUrl;

    /**用户名**/
    private String dbUser;

    /**密码**/
    private String dbPwd;

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPwd() {
        return dbPwd;
    }

    public void setDbPwd(String dbPwd) {
        this.dbPwd = dbPwd;
    }

    @Override
    public String toString() {
        return "DBServerConfig [" + "dbUrl=" + dbUrl +", dbUser='" + dbUser + ", dbPwd='" + dbPwd +"]";
    }
}
