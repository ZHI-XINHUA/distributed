package zkbook.curator.use_scene;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import zkbook.curator.CreateSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by xh.zhi on 2018-9-10.
 */
public class LockDemo {
    private static CuratorFramework client;

    public static void main(String[] args) {
        client = CreateSession.getInstance();
        client.start();

        //noLock(client);

        lock(client);
    }

    /**
     * 没加锁的 流水号生成
     * @param client
     */
    public static void noLock(CuratorFramework client){
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i=0;i<10;i++){
            new Thread(
                    () -> {
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss|SSS");
                        String orderNo = df.format(new Date());
                        System.out.println("生成的订单号："+orderNo);
                    }
            ).start();

            latch.countDown();
        }
    }

    /**
     * 分布式锁
     * @param client
     */
    public static void lock(CuratorFramework client) {
        String path = "/lock_path";
        final CountDownLatch latch = new CountDownLatch(1);
        //锁
        final InterProcessMutex lock = new InterProcessMutex(client, path);
        for (int i = 0; i < 10; i++) {
            new Thread(
                    () -> {
                        try {
                            latch.await();
                            //获得锁
                            lock.acquire();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss|SSS");
                        String orderNo = df.format(new Date());
                        System.out.println("生成的订单号：" + orderNo);

                        //是否锁
                        try {
                            lock.release();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            ).start();

            latch.countDown();

        }
    }
}
