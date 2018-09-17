package zkbook.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 *  读取节点
 */
public class GetNode {

    public static void main(String[] args) throws Exception {
        String path ="/mynode/c1";

        //创建会话
        CuratorFramework client = CreateSession.getInstance();
        client.start();

        //创建临时节点
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path,"init value".getBytes());

        //读取节点
        byte[] bytes = client.getData().forPath(path);
        System.out.println(path+"节点的值为："+new String(bytes));

        //读取节点值和信息stat
        Stat stat = new Stat();
        byte[] bytes1 = client.getData().storingStatIn(stat).forPath(path);
        System.out.println(path+"节点的值为："+new String(bytes1) +";stat为"+stat);


    }
}
