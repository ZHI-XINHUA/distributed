package balance.server;

/**
 * 注册节点服务
 */
public interface RegistProvicer {

    /**
     * 注册服务
     * @param context
     * @throws Exception
     */
    void regist(Object context) throws Exception;


    /**
     * 取消注册
     * @param context
     * @throws Exception
     */
    void unRegist(Object context) throws Exception;
}
