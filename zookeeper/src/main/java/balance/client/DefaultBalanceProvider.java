package balance.client;

import balance.server.ServerData;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  默认负载服务器提供类
 */
public class DefaultBalanceProvider extends AbstractBalanceProvider<ServerData> {
    private static final Integer SESSION_TIME_OUT = 10000;
    private static final Integer CONNECT_TIME_OUT = 10000;


    /**服务器节点**/
    private String serverPath;

    /**zk客户端**/
    private ZkClient zkClient;

    public DefaultBalanceProvider(String zkServer,String serverPath){
        this.serverPath = serverPath;
        zkClient = new ZkClient(zkServer,SESSION_TIME_OUT, CONNECT_TIME_OUT, new SerializableSerializer());
    }

    @Override
    protected ServerData balanceAlgorithm(List<ServerData> items) {
        //找出balance最少的
        if(items==null || items.size()==0){
            return null;
        }

        Collections.sort(items);
        return items.get(0);
    }

    @Override
    protected List<ServerData> getBalanceItems() {
        List<ServerData> serverDataList = new ArrayList<ServerData>();
        List<String> childList = zkClient.getChildren(serverPath);

        for (String child : childList){
            ServerData serverData = zkClient.readData(serverPath.concat("/").concat(child));
            serverDataList.add(serverData);
        }
        return serverDataList;
    }
}
