package zkbook.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xh.zhi on 2018-9-10.
 */
public class CreateNode {
    private static  CuratorFramework client;

    public static void main(String[] args) throws Exception {

        //createSimple();

        createByInBackground();


    }

    public static void createSimple() throws Exception {
        //连接会话
        client =  CreateSession.getInstance();
        client.start();

        //创建节点
        //client.create().withMode(CreateMode.PERSISTENT).forPath("/newnode","value".getBytes());

        //创建一个临时节点，并递归创建父节点
        String result = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/zk/zk_temp","temp".getBytes());

        System.out.println(result);///zk/zk_temp
    }

    public static  void createByInBackground() throws Exception {
        String path = "/zk-book";

         final  CountDownLatch latch = new CountDownLatch(2);

        ExecutorService service = Executors.newFixedThreadPool(2);


        //连接会话
        client =  CreateSession.getInstance();
        client.start();

        //线程池后台处理事件
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("当前的处理线程是："+ Thread.currentThread().getName());
                        System.out.println(Thread.currentThread().getName()+"[事件处理code："+curatorEvent.getResultCode()+"；事件处理类型："+curatorEvent.getType()+"]");
                        //System.out.println(Thread.currentThread().getName()+"[节点数据："+ curatorFramework.getData()+"]");
                        latch.countDown();
                    }
                },service).forPath(path,"init value".getBytes());

        //主线程后台处理事件
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("当前的处理线程是："+ Thread.currentThread().getName());
                        System.out.println(Thread.currentThread().getName()+"[事件处理code："+curatorEvent.getResultCode()+"；事件处理类型："+curatorEvent.getType()+"]");
                        //System.out.println(Thread.currentThread().getName()+"[节点数据："+ curatorFramework.getData()+"]");
                        latch.countDown();

                    }
                }).forPath(path,"init value".getBytes());


        latch.await();
        service.shutdown();
    }
}
