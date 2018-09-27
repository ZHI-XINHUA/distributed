package netty.rpc.core.msg;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 调用服务的约定
 */
public class InvokerMsg  implements Serializable{


    private static final long serialVersionUID = 1224199251950203363L;
    /**类名**/
    private String className;
    /**方法名**/
    private String methodName;
    /**参数**/
    private Class<?>[] params;
    /**参数值**/
    private Object[] values;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParams() {
        return params;
    }

    public void setParams(Class<?>[] params) {
        this.params = params;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "InvokerMsg{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", params=" + Arrays.toString(params) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
