package zkbook.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.data.Stat;

/**
 * 更新节点
 */
public class WriteNode {

    public static void main(String[] args) {
        ZkClient zkClient = ZkConnection.getZkClient();

        String path = "/mydata";
        String value = "mydata";

        //修改节点
        //Stat stat = zkClient.writeData(path,value);

        //带版本号修改，类似CAS原子操作
        Stat stat = zkClient.writeData(path,value+"_1",1);
        System.out.println(stat);
    }
}
