package balance.server;

/**
 * 负载计数器接口
 */
public interface BalanceUpdateProvider {

    /**
     * 负载计数器+1
     * @param step
     * @return
     */
    boolean addBalance(Integer step);

    /**
     * 负载计数器-1
     * @param step
     * @return
     */
    boolean reduceBalance(Integer step);
}
