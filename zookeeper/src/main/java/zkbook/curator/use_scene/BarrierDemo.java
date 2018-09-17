package zkbook.curator.use_scene;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import zkbook.curator.CreateSession;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by xh.zhi on 2018-9-11.
 */
public class BarrierDemo {
    static  DistributedBarrier barrier ;


    public static void main(String[] args) {
        //jdkCyclicBarrier();

       //distributedBarrier();

        distributedDoubleBarrier();
    }

    /**
     * java并发包下CyclicBarrier使用
     */
    public static void jdkCyclicBarrier(){
        CyclicBarrier cyclicBarrier =  new CyclicBarrier(9);
        ExecutorService executor = Executors.newFixedThreadPool(9);

        for(int i=1;i<=9;i++){
            String runer = i+"号选手";
            executor.submit(() ->{
                System.out.println(runer+"准备好了！");

                try {
                    //等待9位选手都已准备好
                    cyclicBarrier.await();

                    TimeUnit.SECONDS.sleep(1);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(runer+"起跑！");
            });
        }

        executor.shutdown();
    }

    /**
     * 分布式barrier -》DistributedBarrier
     */
    public static void distributedBarrier(){
        String path ="/barrier_path";

        for(int i=1;i<=9;i++){
            String runer = i+"号选手";
            new Thread(
                    () -> {
                        //创建会话
                        CuratorFramework client = CreateSession.getInstance();
                        client.start();

                        barrier = new DistributedBarrier(client,path);

                        System.out.println(runer+"准备好了！");
                        try {
                            //设置barrier
                            barrier.setBarrier();
                            //等待barrier释放
                            barrier.waitOnBarrier();

                            TimeUnit.SECONDS.sleep(1);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println(runer+"起跑！");
                    }

            ).start();
        }

        try {
            TimeUnit.SECONDS.sleep(3);
            //释放barrier
            barrier.removeBarrier();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 分布式栏栅 -》 DistributedDoubleBarrier
     */
    public static void distributedDoubleBarrier(){
        String path = "/doubleBarrier_path";

        for(int i=1;i<=9;i++){
            String runer = i+"号选手";
            new Thread(
                    () -> {
                        //创建会话
                        CuratorFramework client = CreateSession.getInstance();
                        client.start();

                         DistributedDoubleBarrier doubleBarrier = new DistributedDoubleBarrier(client,path,9);

                        System.out.println(runer+"准备好了！");

                        try {
                            //进入等待
                            doubleBarrier.enter();

                            TimeUnit.SECONDS.sleep(3);

                            //释放
                            doubleBarrier.leave();

                            System.out.println(runer+"起跑！");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            ).start();
        }
    }

}
