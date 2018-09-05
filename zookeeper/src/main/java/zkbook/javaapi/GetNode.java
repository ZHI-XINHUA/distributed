package zkbook.javaapi;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GetNode {
    private static ZooKeeper zooKeeper = null;
    //服务器连接地址
    private static  String host = "192.168.1.106:2181,192.168.1.108:2181,192.168.1.109:2181";
    //超时时间
    private static int sessionTimeout=5000;

    public  static CountDownLatch countDownLatch = new CountDownLatch(1);


    static {
        try {
            //连接服务器
            zooKeeper = new ZooKeeper(host,sessionTimeout,new MyGetWatch());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        countDownLatch.await();

        getNode1();
    }

    private static void getNode1(){
        try {

            //获取子节点列表
            List<String> childNodes= zooKeeper.getChildren("/",false);
            System.out.println("根目录子节点列表："+childNodes);

            List<String> childNodes1=zooKeeper.getChildren("/",new MyGetWatch());
            System.out.println("根目录子节点列表："+childNodes1);


           TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class  MyGetWatch implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.getState());
        if(Event.KeeperState.SyncConnected==watchedEvent.getState()){
            System.out.println("ZooKeeper 链接成功！");


            if(watchedEvent.getType()==Event.EventType.None && watchedEvent.getPath()==null){
                GetNode.countDownLatch.countDown();
            }else if(watchedEvent.getType()==Event.EventType.NodeChildrenChanged){
                System.out.println(watchedEvent.getPath());
            }
        }
    }
}
