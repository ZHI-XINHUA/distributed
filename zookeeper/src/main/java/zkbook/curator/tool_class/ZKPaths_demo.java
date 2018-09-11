package zkbook.curator.tool_class;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;
import zkbook.curator.CreateSession;

import java.util.zip.ZipOutputStream;

/**
 * Created by xh.zhi on 2018-9-11.
 */
public class ZKPaths_demo {

    public static void main(String[] args) throws Exception {
        String path = "/zk_path";
        //创建会话
        CuratorFramework client = CreateSession.getInstance();
        client.start();

        //获取zookeeper
        ZooKeeper zooKeeper = client.getZookeeperClient().getZooKeeper();

        //固定命名空间拼接path

        System.out.println(ZKPaths.fixForNamespace(path,"ch"));

        System.out.println(ZKPaths.makePath(path,"ch1"));

        System.out.println( ZKPaths.getPathAndNode(path+"/ch1"));

        //创建持久化节点
        //ZKPaths.mkdirs(zooKeeper,path+"/ch1");

        //ZKPaths.mkdirs(zooKeeper,"/zk_path3/ch3",false);

        //System.out.println( ZKPaths.getSortedChildren(zooKeeper,path));


        ZKPaths.deleteChildren(zooKeeper,path,false);


    }
}
