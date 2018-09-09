package zkbook.zkclient;

import org.I0Itec.zkclient.ZkClient;

/**
 * 删除节点
 */
public class DeleteNode {

    public static void main(String[] args) {
        //建立会话连接
        ZkClient zkClient = ZkConnection.getZkClient();

        //删除节点，有子节点删除失败
        //boolean delFlag = zkClient.delete("/mydata");

        //连同子节点删除
        boolean delFlag = zkClient.deleteRecursive("/mydata");


        System.out.println("delete node "+delFlag);
    }
}
