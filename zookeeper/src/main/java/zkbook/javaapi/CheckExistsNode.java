package zkbook.javaapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;



/**
 * Created by xh.zhi on 2018-9-6.
 */
public class CheckExistsNode {

    public static ZooKeeper zooKeeper = null;

    public static void main(String[] args) {
        //exists1();
        //exists2();
        //exists3();

        exists4();
    }


    /**
     * 同步检查
     */
    private static void exists1(){
        zooKeeper = ConnectionZk.getZkConnection();
        try {
            String path ="/data1";
            Stat stat = zooKeeper.exists(path,true);

            printStat(path,stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步检查并添加监听
     */
    private static void exists2(){
        zooKeeper = ConnectionZk.getZkConnection();
        String path ="/data1";
        try {
            //检查节点是否存在，并监听事件
            Stat stat= zooKeeper.exists(path,new CheckMyWatcher());
            printStat(path,stat);

            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 异步检查
     */
    private static void exists3(){
        zooKeeper = ConnectionZk.getZkConnection();
        String path ="/data1";
        zooKeeper.exists(path,true,new CheckMyCallback(),"checkNode");

        try {
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步检查并监听
     */
    private static void exists4(){
        zooKeeper = ConnectionZk.getZkConnection();
        String path ="/data1";
        zooKeeper.exists(path,new CheckMyWatcher(),new CheckMyCallback(),"checkNode");

        try {
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 打印节点
     * @param path
     * @param stat
     */
    public  static void printStat(String path,Stat stat){
        if(stat==null){
            System.out.println("stat is not exists！");
            return;
        }
        System.out.println("存在【"+path+"】节点");
        try {

            byte[] bytes =  zooKeeper.getData(path,true,stat);
            String data = new String(bytes);
            System.out.println("节点值："+data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("节点信息：[Aversion="+stat.getAversion()+";Cversion="+stat.getCversion()+";DataLength="+stat.getDataLength()+";NumChildren="+stat.getNumChildren()+
                ";Version="+stat.getVersion()+";Ctime="+stat.getCtime()+";Czxid="+stat.getCzxid()+";EphemeralOwner="+stat.getEphemeralOwner()+";Mtime="+stat.getMtime()+
                ";tMzxid="+stat.getMzxid()+";Pzxid="+stat.getPzxid()
        );
    }
}

/**
 * 自定义watcher
 */
class CheckMyWatcher implements Watcher{

    @Override
    public void process(WatchedEvent watchedEvent) {
        String path = watchedEvent.getPath();
        if(watchedEvent.getType()== Event.EventType.NodeCreated){
            System.out.println("节点【"+path+"】:创建");
        }else if(watchedEvent.getType()== Event.EventType.NodeDataChanged){
            System.out.println("节点【"+path+"】:更新");
        }else if(watchedEvent.getType()== Event.EventType.NodeDeleted){
            System.out.println("节点【"+path+"】:删除");
        }
    }
}

/**
 * 异步回调
 */
class CheckMyCallback implements AsyncCallback.StatCallback{

    @Override
    public void processResult(int status, String path, Object ctx, Stat stat) {
        System.out.println("异步回调结果：status"+status+";ctx="+ctx+";path="+path+";stat="+stat);
    }
}
