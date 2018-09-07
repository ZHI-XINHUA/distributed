package zkbook.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xh.zhi on 2018-9-7.
 *  新建节点
 */
public class CreateNode {
    private static ZkClient zkClient;

    public static void main(String[] args) {
        zkClient = ZkConnection.getZkClient();

        String nodePrefix ="/create";
        String valuePrefix ="value";

        //1、创建持久化节点  CreateMode：节点模式（持久化节点、有序持久化节点、临时节点和有序临时节点），默认OPEN_ACL_UNSAFE权限
        //String creteNode = zkClient.create(nodePrefix+"_1",valuePrefix+"_1", CreateMode.PERSISTENT);

        //2、创建临时有序节点，并且赋予 能创建子节点权限（注意：临时节点不能创建子节点）
        //List list = new ArrayList();
        //list.add(new Id("dd","d"));
        //String creteNode = zkClient.create(nodePrefix+"_2",valuePrefix+"_2", ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        //3、创建一个空值的临时节点  this.create(path, (Object)null, CreateMode.EPHEMERAL);
        //zkClient.createEphemeral(nodePrefix+"_3");

        //4、创建临时节点
        //zkClient.createEphemeral(nodePrefix+"_4",valuePrefix+"_4");

        //5、创建临时节点，赋予READ_ACL_UNSAFE权限
        //zkClient.createEphemeral(nodePrefix+"_5", ZooDefs.Ids.READ_ACL_UNSAFE);

        //6、创建有值的临时节点，赋予READ_ACL_UNSAFE权限
        //zkClient.createEphemeral(nodePrefix+"_6",valuePrefix+"_6", ZooDefs.Ids.READ_ACL_UNSAFE);

        //7、创建有序临时节点
        //String createNode =  zkClient.createEphemeralSequential(nodePrefix+"_7",valuePrefix+"_7");

        //8、带权限的有序临时节点
        //zkClient.createEphemeralSequential(nodePrefix+"_8",valuePrefix+"_8", ZooDefs.Ids.READ_ACL_UNSAFE);

        //9、null 值的持久化节点
        //zkClient.createPersistent(nodePrefix+"_9");

        //10、有值的持久化节点
        //zkClient.createPersistent(nodePrefix+"_10",valuePrefix+"_10");

        //11、创建null值的持久化节点，并且指定是否为父节点
        //zkClient.createPersistent(nodePrefix+"_11",true);

        //12、创建持久化节点，并设置只读权限
        //zkClient.createPersistent(nodePrefix+"_12",valuePrefix+"_12", ZooDefs.Ids.READ_ACL_UNSAFE);

        //13、创建有序持久化节点，并设置只读权限
        zkClient.createPersistentSequential(nodePrefix+"_13",valuePrefix+"_13", ZooDefs.Ids.READ_ACL_UNSAFE);

        //14、创建有序持久化节点
        zkClient.createPersistentSequential(nodePrefix+"_14",valuePrefix+"_14");

        //System.out.println(creteNode);

        List<String> rootNodes = zkClient.getChildren("/");
        System.out.println(rootNodes);

    }
}
