package zkbook.curator.tool_class;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.CreateMode;

/**
 * Created by xh.zhi on 2018-9-11.
 * 单元测试：简单启动一个服务器zookeeper
 */
public class Testing_Server_Demo {
    static String path = "/zookeeper";
    public static void main(String[] args) throws Exception {

        TestingServer server = new TestingServer(2181, new java.io.File("/tmp/zookeeper1"));

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(server.getConnectString())
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/test","zzz".getBytes());


        System.out.println(new String( client.getData().forPath("/test")));
        System.out.println( client.getChildren().forPath( path ));
        server.close();
    }
}
