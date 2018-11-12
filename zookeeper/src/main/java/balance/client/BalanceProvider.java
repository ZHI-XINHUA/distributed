package balance.client;

/**
 * 客户端balance接口
 */
public interface BalanceProvider<T> {

    /**
     * 获取服务器（负载计算后的）
     * @return
     */
    public T getBalanceItem();
}
