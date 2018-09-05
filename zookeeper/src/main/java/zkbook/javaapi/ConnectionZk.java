package zkbook.javaapi;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.apache.zookeeper.Watcher.Event.*;

/**
 * Created by xh.zhi on 2018-9-5.
 */
public class ConnectionZk implements Watcher{
    //服务器连接地址
    private static  String host = "192.168.100.51:2181,192.168.100.59:2181,192.168.100.84:2181";
    //超时时间
    private static int sessionTimeout= 5000;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    //会话id
    private static long sessionId;
    //会话秘钥
    private static byte[] sessionPassw;

    public static void main(String[] args) throws InterruptedException {

         connectionServer1();

        //connectionServer2();

        //睡眠3秒，让上面的链接创建，再获取链接会话id和秘钥
        TimeUnit.SECONDS.sleep(3);
        System.out.println("========链接当前会话========");
        connectionServe3();
    }

    /**
     * 连接方式一
     */
    private static void connectionServer1(){
        try {
            ZooKeeper zooKeeper = new ZooKeeper(host,sessionTimeout,new ConnectionZk());

            System.out.println(zooKeeper.getState());

            //等待，知道连接成功或连接超时
            countDownLatch.await();

            System.out.println("连接成功");

           // String result = zooKeeper.create("/javaapi_node","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            //System.out.println("创建节点："+result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  支持只读模式，注意：是支持只读模式，不是只读模式，当zookeeper发生故障后选举leader时，开启只读模式，避免客户端发起cud操作
     */
    private static void connectionServer2(){
        try {
            ZooKeeper zooKeeper = new ZooKeeper(host,sessionTimeout,new ConnectionZk(),true);
            System.out.println(zooKeeper.getState());
            //等待，知道连接成功或连接超时
            countDownLatch.await();

            System.out.println("连接成功");

            //获取会话id
            sessionId =  zooKeeper.getSessionId();
            //获取会话秘钥
            sessionPassw = zooKeeper.getSessionPasswd();

            String result = zooKeeper.create("/javaapi_node2","test1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("创建节点："+result);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    private static void connectionServe3(){
        try {
            ZooKeeper zooKeeper = new ZooKeeper(host,sessionTimeout,new ConnectionZk(),sessionId,sessionPassw);
            System.out.println(zooKeeper.getState());

            //等待，知道连接成功或连接超时
            countDownLatch.await();


            System.out.println("连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听事件
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.getState());
        //连接成功
        if(KeeperState.SyncConnected == watchedEvent.getState()){
            countDownLatch.countDown();
        }
    }
}
