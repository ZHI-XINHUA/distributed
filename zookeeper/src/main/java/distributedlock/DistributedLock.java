package distributedlock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口
 */
public interface DistributedLock {

    /**
     * 获取锁，没有获得则等待
     * @throws Exception
     */
    void require() throws Exception;

    /**
     * 一定时间内获取锁
     * @param time
     * @param timeUnit
     * @throws Exception
     */
    void require(long time, TimeUnit timeUnit) throws Exception;

    /**
     * 释放锁
     * @throws Exception
     */
    void release() throws Exception;
}
