package balance.client;

import java.util.List;

/**
 * 抽象balance提供者
 */
public abstract class AbstractBalanceProvider<T> implements BalanceProvider {
    /**
     * 获取负载服务器（算法技术负载服务器）
     * @param items
     * @return
     */
    protected abstract T balanceAlgorithm(List<T> items);

    /**
     * 获取服务器list
     * @return
     */
    protected abstract List<T> getBalanceItems();

    @Override
    public Object getBalanceItem() {
        return balanceAlgorithm(getBalanceItems());
    }
}
