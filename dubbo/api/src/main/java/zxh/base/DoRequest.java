package zxh.base;

import java.io.Serializable;

/**
 * Created by xh.zhi on 2018-10-12.
 * 请求实体类
 */
public class DoRequest implements Serializable {
    private static final long serialVersionUID = 8520297113617826914L;

    /**请求参数名称**/
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "DoRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}