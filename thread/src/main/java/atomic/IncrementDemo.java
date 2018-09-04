package atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程原子性问题
 */
public class IncrementDemo {
    private static int count = 0;
    private static int syncCount = 0;
    //让线程间对静态变量可见
    private static volatile int volCount = 0;
    private static AtomicInteger atomicCount= new AtomicInteger(0);


    public static void main(String[] args) throws Exception{
        //开启10个线程
        for (int i=0;i<10;i++){
            new Thread(() -> {
                //执行1000次 +1操作
                for (int n =0;n<1000;n++){
                    count++;

                    //同步方法
                    increateNum();

                    //volatile 修饰，让线程可见，但是不保证原子性
                    volCount++;

                    //jdk api提供的原子类
                    atomicCount.getAndIncrement();
                }
            }).start();
        }

        //主线程睡眠3秒，让子线程保证可以执行完成
        TimeUnit.SECONDS.sleep(5);

        //正确值应该是10000，但是结果会出现少于10000
        System.out.println("10个线程分别执行1000次+1之后，count="+count);

        //同步方法解决多线程并发，但是效率比较低
        System.out.println("10个线程分别执行1000次+1之后，syncCount="+syncCount);

        //volatile 让线程对变量可见，但是不保证其原子性
        System.out.println("10个线程分别执行1000次+1之后，volCount="+volCount);

        //java api提交的原子操作类，底层使用的是unsafe，保证了数据的原子性，效率也比较高
        System.out.println("10个线程分别执行1000次+1之后，atomicCount="+atomicCount);

    }

    /**
     * 同步操作+1
     */
    public static synchronized void increateNum(){
        syncCount ++;
    }


}


