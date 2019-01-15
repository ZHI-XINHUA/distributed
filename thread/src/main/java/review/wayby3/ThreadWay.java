package review.wayby3;

/**
 * 实现线程方式一：继承Thread接口
 */
public class ThreadWay extends Thread {

    @Override
    public void run() {
        System.out.println("继承Thread，重写run方法>>>当前线程："+Thread.currentThread().getName());
    }

}


