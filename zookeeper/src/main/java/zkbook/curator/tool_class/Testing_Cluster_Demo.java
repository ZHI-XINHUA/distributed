package zkbook.curator.tool_class;

import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingZooKeeperServer;

import java.util.concurrent.TimeUnit;

/**
 * Created by xh.zhi on 2018-9-11.
 * 单元测试：集群机器环境
 */
public class Testing_Cluster_Demo {
    public static void main(String[] args) throws Exception {
        //实例化三台机器
        TestingCluster cluster = new TestingCluster(3);
        //启动
        cluster.start();

        //模拟启动时间
        TimeUnit.SECONDS.sleep(3);

        //声明一台zookeeper leader 服务器
        TestingZooKeeperServer leader = null;
        
        //遍历服务器
        for (TestingZooKeeperServer server:cluster.getServers()) {
            System.out.println("serverid="+server.getInstanceSpec().getServerId()+"  role="+server.getQuorumPeer().getServerState()
            +" path="+server.getInstanceSpec().getDataDirectory().getAbsolutePath());

            //获取leader节点
            if("leading".equals(server.getQuorumPeer().getServerState())){
                leader = server;
            }
        }

        //模拟leader节点宕机
        leader.kill();
        TimeUnit.SECONDS.sleep(3);
        System.out.println(" after leader kill:");
        for (TestingZooKeeperServer server:cluster.getServers()) {
            System.out.println("serverid="+server.getInstanceSpec().getServerId()+"  role="+server.getQuorumPeer().getServerState()
                    +" path="+server.getInstanceSpec().getDataDirectory().getAbsolutePath());


        }


        //关闭集群机器
        cluster.stop();

    }
}
