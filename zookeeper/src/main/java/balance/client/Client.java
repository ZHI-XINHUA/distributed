package balance.client;

/**
 * 客户端接口
 */
public interface Client {

    /**
     * 连接
     * @throws Exception
     */
    public void connect() throws Exception;

    /**
     * 端口连接
     * @throws Exception
     */
    public void disConnect() throws Exception;
}
