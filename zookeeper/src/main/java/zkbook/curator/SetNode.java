package zkbook.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Created by xh.zhi on 2018-9-10
 * 更新节点.
 */
public class SetNode {

    public static void main(String[] args) throws Exception {
        String path ="/mynode/c1";

        //创建会话
        CuratorFramework client = CreateSession.getInstance();
        client.start();

        //创建临时
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path,"init value".getBytes());

        //读取节点值
        System.out.println("更新前节点值："+new String(client.getData().forPath(path)));

        //更新节点
        client.setData().forPath(path," For the first time value".getBytes());

        //读取节点值
        Stat stat = new Stat();
        byte[] bytes = client.getData().storingStatIn(stat).forPath(path);
        System.out.println("第一次更新后节点值："+new String(bytes));

        //更新节点，带版本更新，类似cvs操作
        client.setData().withVersion(stat.getVersion()).forPath(path,"The second time value".getBytes());

        System.out.println("第二次更新后节点值："+new String(client.getData().forPath(path)));

    }
}
