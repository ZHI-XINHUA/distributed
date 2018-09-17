package zkbook.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by xh.zhi on 2018-9-10.
 * 创建会话
 */
public class CreateSession {
    /** 服务器连接地址 **/
    private static  String host = "192.168.100.84:2181,192.168.100.51:2181,192.168.100.59:2181";

    /**连接超时时间**/
    static int connectionTimeOut = 50000;

    /**会话超时海事局**/
    static int sessionTimeOut = 5000;

    public static void main(String[] args) {
        simpleSession();

        //fluentSession();
    }


    public static CuratorFramework getInstance(){
        return fluentSession();
    }

    /**
     * 创建简单会话
     */
    private static void  simpleSession(){
        //重试机制
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        //创建会话
        CuratorFramework client = CuratorFrameworkFactory.newClient(host,sessionTimeOut,connectionTimeOut,retryPolicy);
        client.start();

        System.out.println(client.getState());
    }

    /**
     * 创建fluent会话
     */
    private static CuratorFramework  fluentSession(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(host)
                .sessionTimeoutMs(sessionTimeOut)
                .connectionTimeoutMs(connectionTimeOut)
                .retryPolicy(retryPolicy)
                .build();
       // client.start();
        System.out.println(client);

      return client;

    }

    /**
     * 指定会话命名空间
     */
    private static void nameSpaceSession(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(host)
                .sessionTimeoutMs(sessionTimeOut)
                .connectionTimeoutMs(connectionTimeOut)
                .retryPolicy(retryPolicy)
                .namespace("mydata")  //添加独立的命名空间   这里的会话都是操作/mydata路径下的
                .build();
        client.start();

    }

}
