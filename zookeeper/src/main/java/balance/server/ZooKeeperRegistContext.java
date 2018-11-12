package balance.server;

import org.I0Itec.zkclient.ZkClient;

/**
 *  zookeeper服务注册的上下文
 */
public class ZooKeeperRegistContext {

    /**注册的路径**/
    private String path;

    /**客户端**/
    private ZkClient zkClient;

    /**服务器信息**/
    private ServerData serverData;

    public ZooKeeperRegistContext(String path, ZkClient zkClient, ServerData serverData) {
        this.path = path;
        this.zkClient = zkClient;
        this.serverData = serverData;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public ServerData getServerData() {
        return serverData;
    }

    public void setServerData(ServerData serverData) {
        this.serverData = serverData;
    }
}
