package zkbook.javaapi;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DeleteNode {
    private static ZooKeeper zooKeeper = null;
    //服务器连接地址
    private static  String host = "192.168.1.106:2181,192.168.1.108:2181,192.168.1.109:2181";
    //超时时间
    private static int sessionTimeout=5000;

    public  static CountDownLatch countDownLatch = new CountDownLatch(1);

    static {
        try {
            //连接服务器
            zooKeeper = new ZooKeeper(host,sessionTimeout,new MyDeleteWatch());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        countDownLatch.await();

        //deleteNode1();

        deletNode2();
    }

    private static void  deleteNode1(){
        try {
            zooKeeper.delete("/zxh",1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    private static void deletNode2() throws InterruptedException {
        //异步删除
        zooKeeper.delete("/data",0,new DeleteVoidCallback(),"delete operation");

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);

    }
}

class DeleteVoidCallback implements AsyncCallback.VoidCallback{

    @Override
    public void processResult(int rc, String path, Object cxt) {
        System.out.println("rc="+rc+";path="+path+";ctx"+cxt);
    }
}

class  MyDeleteWatch implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.getState());
        if(Event.KeeperState.SyncConnected==watchedEvent.getState()){
            System.out.println("ZooKeeper 链接成功！");
            DeleteNode.countDownLatch.countDown();
        }
    }
}
