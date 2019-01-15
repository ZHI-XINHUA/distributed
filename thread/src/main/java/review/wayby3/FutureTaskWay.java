package review.wayby3;

import java.util.concurrent.Callable;

/**
 * 方式三：通过Callable和FutureTask创建线程
 */
public class FutureTaskWay implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "FutureTask创建线程 >>"+Thread.currentThread().getName();
    }
}
