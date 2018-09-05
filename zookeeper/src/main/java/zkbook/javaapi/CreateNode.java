package zkbook.javaapi;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by xh.zhi on 2018-9-5.
 */
public class CreateNode {
    private static ZooKeeper zooKeeper = null;
    //服务器连接地址
    private static  String host = "192.168.1.106:2181,192.168.1.108:2181,192.168.1.109:2181";
    //超时时间
    private static int sessionTimeout=5000;

    public  static CountDownLatch countDownLatch = new CountDownLatch(1);

    static {
        try {
            //连接服务器
            zooKeeper = new ZooKeeper(host,sessionTimeout,new MyWatch());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        countDownLatch.await();


        //create1();

        create2();
    }

    private static void create1(){
        try {
            String path = zooKeeper.create("/mydata11","hello".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL);
            System.out.println("创建节点："+path);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void create2() throws IOException {
        //异步创建节点
        zooKeeper.create("/zxh55","hello".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                new MyStringCallback(),
                "test");

        System.in.read();
    }
}

class MyStringCallback implements AsyncCallback.StringCallback{

    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("StringCallback:[rc="+rc+",path="+path+",ctx="+ctx+",name="+name+"]");
    }
}

class  MyWatch implements Watcher{
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.getState());
        if(Event.KeeperState.SyncConnected==watchedEvent.getState()){
            System.out.println("ZooKeeper 链接成功！");
            CreateNode.countDownLatch.countDown();
        }
    }
}
