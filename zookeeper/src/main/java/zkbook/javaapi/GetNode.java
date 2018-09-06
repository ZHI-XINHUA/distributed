package zkbook.javaapi;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GetNode {
    private static ZooKeeper zooKeeper = null;


    public static void main(String[] args) throws InterruptedException {
        //getNode();

        getChildNode();
    }

    /**
     * 获取节点
     */
    private static void getNode(){
       zooKeeper = ConnectionZk.getZkConnection();

       String path = "/mydata";
        try {
            byte[] bytes1 = zooKeeper.getData(path,true,null);
            System.out.println("节点【"+path+"】的值为："+new String(bytes1));

            System.out.println("================================");
            byte[] bytes2 = zooKeeper.getData(path,new GetNodeWatcher(),null);
            System.out.println("节点【"+path+"】的值为："+new String(bytes2));

            System.out.println("================================");
            zooKeeper.getData(path,true,new GetDataCallback(),"get Node");

            System.out.println("================================");
            zooKeeper.getData(path,new GetNodeWatcher(),new GetDataCallback(),"get Node xxx");

            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取子节点
     */
    private static void getChildNode(){
        zooKeeper = ConnectionZk.getZkConnection();

        String path = "/mydata";
        try {
            List<String>  childNodes = zooKeeper.getChildren(path,true);
            System.out.println(path+"子节点有："+childNodes);
            System.out.println("=========================");

            List<String>  childNodes1 = zooKeeper.getChildren(path,new GetNodeWatcher());
            System.out.println(path+"子节点有："+childNodes1);
            System.out.println("=========================");

            zooKeeper.getChildren(path,new GetNodeWatcher(),new MyChildrenCallback()," call ChildrenCallback");

            System.out.println("=========================");
            zooKeeper.getChildren(path,new GetNodeWatcher(),new MyChildren2Callback()," call MyChildren2Callback");

            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 事件监听
 */
class GetNodeWatcher implements Watcher{

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.getType());
        String path = watchedEvent.getPath();
        if(watchedEvent.getType()== Event.EventType.NodeCreated){
            System.out.println("创建节点【"+path+"】");
        }else if(watchedEvent.getType()== Event.EventType.NodeDataChanged){
            System.out.println("修改节点【"+path+"】");
        }else if(watchedEvent.getType()== Event.EventType.NodeDeleted){
            System.out.println("删除节点【"+path+"】");
        }else if(watchedEvent.getType()==Event.EventType.NodeChildrenChanged){//创建/删除子节点 触发，修改子节点不触发
            System.out.println("【"+path+"】创建/删除子节点");
        }
    }
}

/**
 * 回调
 */
class GetDataCallback implements AsyncCallback.DataCallback{
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] bytes, Stat stat) {
        System.out.println("StringCallback:[rc="+rc+",path="+path+",ctx="+ctx+",stat="+stat+",bytes="+new String(bytes)+"]");
    }
}

class MyChildrenCallback implements AsyncCallback.ChildrenCallback{

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> list) {
        System.out.println("MyChildrenCallback:[rc="+rc+",path="+path+",ctx="+ctx+",list="+list);
    }
}

class MyChildren2Callback implements AsyncCallback.Children2Callback{

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> list, Stat stat) {
        System.out.println("MyChildren2Callback:[rc="+rc+",path="+path+",ctx="+ctx+",list="+list+",stat="+stat);
    }
}


