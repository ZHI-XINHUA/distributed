package zkbook.zkclient;

import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.TimeUnit;

/**
 * 检查节点是否存在
 */
public class CheckExistsNode {

    public static void main(String[] args) {
        ZkClient zkClient = ZkConnection.getZkClient();

        String path = "/mydata";
        //检查节点是否存在
        //boolean checkExists = zkClient.exists(path);

        //检查节点是否存在，设定超时时间，超过规定时间没结果则认为不存在节点
        boolean checkExists =  zkClient.waitUntilExists(path,TimeUnit.SECONDS,3);
        System.out.println(path+"节点："+(checkExists?"存在":"不存在"));
    }
}
