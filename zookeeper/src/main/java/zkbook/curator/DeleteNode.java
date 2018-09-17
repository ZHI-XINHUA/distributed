package zkbook.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Created by xh.zhi on 2018-9-10.
 */
public class DeleteNode {

    public static void main(String[] args) throws Exception {
        //创建会话
        CuratorFramework client = CreateSession.getInstance();
        client.start();

        String path="/zk_b";
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path,"init".getBytes());

        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        System.out.println(stat);

        //1、删除节点，不存在时会报错
         //client.delete().forPath(path);

        //2、递归删除节点
        //client.delete().deletingChildrenIfNeeded().forPath(path);

        //3、指定版本删除
        //client.delete().withVersion(stat.getVersion()).forPath(path);

        //4、强制保证删除节点，guaranteed()是一个保障措施，只要会话有效，那么curator会在后台持续进行删除操作，直到节点删除成功
        client.delete().guaranteed().forPath(path);

    }
}
