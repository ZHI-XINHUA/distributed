package zxh.base;

import java.io.Serializable;

/**
 * Created by xh.zhi on 2018-10-12.
 * 响应封装实体类
 */
public class DoResponse implements Serializable {
    private static final long serialVersionUID = -3633484250078108955L;

    /**响应信息**/
    private Object result;

    /**响应状态码**/
    private String code;

    /**备注**/
    private String demo;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String toString() {
        return "DoResponse{" +
                "result=" + result +
                ", code='" + code + '\'' +
                ", demo='" + demo + '\'' +
                '}';
    }
}