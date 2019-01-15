package review.wayby3;

/**
 * 方式二：实现Runnable接口
 */
public class RunableWay implements Runnable {
    @Override
    public void run() {
        System.out.println("实现Runnable接口>>当前线程："+Thread.currentThread().getName());
    }
}
