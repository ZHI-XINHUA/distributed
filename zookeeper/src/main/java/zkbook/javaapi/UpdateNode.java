package zkbook.javaapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class UpdateNode {
    private static ZooKeeper zooKeeper = null;
    //服务器连接地址
    private static  String host = "192.168.1.106:2181,192.168.1.108:2181,192.168.1.109:2181";
    //超时时间
    private static int sessionTimeout=5000;

    public  static CountDownLatch countDownLatch = new CountDownLatch(1);


    static {
        try {
            //连接服务器
            zooKeeper = new ZooKeeper(host,sessionTimeout,new MyUpdateWatch());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        countDownLatch.await();

        //update1();

        update2();

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);

    }

    private static void update1(){
        try {
            //同步更新
            Stat stat = zooKeeper.setData("/mydata","test".getBytes(),0);
            System.out.println(stat);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void update2(){
        //异步更新
        zooKeeper.setData("/mydata","test111".getBytes(),1,new MyStatCallBack(),"update mydata");
    }



}

/**
 * 异步更新回调
 */
class MyStatCallBack implements AsyncCallback.StatCallback{

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        System.out.println(i+";"+s+";"+o+";"+stat);
    }
}

/**
 * 注册监听
 */
class  MyUpdateWatch implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.getState());
        if(Event.KeeperState.SyncConnected==watchedEvent.getState()){
            System.out.println("ZooKeeper 链接成功！");
            UpdateNode.countDownLatch.countDown();
        }
    }
}
