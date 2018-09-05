package zkbook.javaapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by xh.zhi on 2018-9-5.
 */
public class CreateZk {
    private static ZooKeeper zooKeeper = null;
    //服务器连接地址
    private static  String host = "192.168.100.51:2181,192.168.100.59:2181,192.168.100.84:2181";
    //超时时间
    private static int sessionTimeout=5000;

    public  static CountDownLatch countDownLatch = new CountDownLatch(1);

    static {
        try {
            zooKeeper = new ZooKeeper(host,sessionTimeout,new MyWatch());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        countDownLatch.await();
        System.out.println("ZooKeeper 链接成功！");
        System.out.println(zooKeeper);
    }
}

class  MyWatch implements Watcher{
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.getState());
        if(Event.KeeperState.SyncConnected==watchedEvent.getState()){
            CreateZk.countDownLatch.countDown();
        }
    }
}
