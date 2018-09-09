package zkbook.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 读取节点
 */
public class GetNode {


    public static void main(String[] args) throws Exception{
        ZkClient zkClient = ZkConnection.getZkClient();

       //getChild(zkClient);

       boolean delflag = zkClient.deleteRecursive("/newmydata");

        getData(zkClient);
    }

    /**
     * 读取子节点
     */
    public static void getChild(ZkClient zkClient) throws Exception{
        String path = "/newmydata";

        //设置子节点订阅
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> list) throws Exception {
                System.out.println("订阅消息：parentPath="+parentPath+";list="+list);
            }
        });

        //创建节点
        zkClient.createPersistent(path,"newmydata");
        System.out.println("创建子节点【"+path+"】");
        TimeUnit.SECONDS.sleep(1);



        //创建子节点
        zkClient.createPersistent(path+"/c1","c1");
        System.out.println("创建子节点【"+path+"/c1】");

        TimeUnit.SECONDS.sleep(1);

        zkClient.createPersistent(path+"/c2","c2");
        System.out.println("创建子节点【"+path+"/c2】");

        TimeUnit.SECONDS.sleep(1);

        zkClient.writeData(path+"/c1","c1ddd");
        System.out.println("修改子节点【"+path+"/c1】");

        TimeUnit.SECONDS.sleep(1);

        //读取子节点
        List<String> childs = zkClient.getChildren(path);
        System.out.println("获取"+path+"子节点："+childs);

        TimeUnit.SECONDS.sleep(1);

        // 触发n+1次订阅  n：子节点格式
        //boolean delflag = zkClient.deleteRecursive(path);

        zkClient.delete(path+"/c1");
        System.out.println("删除子节点【"+path+"/c1】");


        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);



    }

    /**
     * 读取节点数据
     */
    public static void getData(ZkClient zkClient) throws Exception{
        String path = "/newmydata";

        //节点数据订阅
        zkClient.subscribeDataChanges(path, new IZkDataListener() {

            //数据发生改变是触发
            @Override
            public void handleDataChange(String path, Object o) throws Exception {
                System.out.println("节点数据订阅："+path+"的值被更新为："+o);
            }

            //节点删除时触发
            @Override
            public void handleDataDeleted(String path) throws Exception {
                System.out.println("节点数据订阅："+path+"节点被删除");
            }
        });

        //创建节点
        zkClient.createPersistent(path,"newmydata");
        System.out.println("创建子节点【"+path+"】");

        TimeUnit.SECONDS.sleep(1);

       //读取节点值
        String value = zkClient.readData(path);
        System.out.println(value);

        TimeUnit.SECONDS.sleep(1);

        //读取节点值，如果节点不存在则返回null
        String value1 = zkClient.readData(path+"dd",true);
        System.out.println(value1);

        TimeUnit.SECONDS.sleep(1);

        //更新节点值
        Stat stat = zkClient.writeData(path,"/ new data");
        System.out.println("更新"+path+"的值");

        TimeUnit.SECONDS.sleep(1);

        //读取节点值，传入Stat，在执行过程中，新的Stat体会原来的
        String value3 = zkClient.readData(path,stat);
        System.out.println(value3);
        System.out.println(stat);

        TimeUnit.SECONDS.sleep(1);

        zkClient.delete(path);
        System.out.println("删除节点"+path);

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}
