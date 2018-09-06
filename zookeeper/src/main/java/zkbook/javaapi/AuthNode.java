package zkbook.javaapi;

import org.apache.zookeeper.*;

/**
 * Created by xh.zhi on 2018-9-6.
 */
public class AuthNode {
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) {
        //operation1();

        delNode();
    }

    private static void operation1(){
        zooKeeper = ConnectionZk.getZkConnection();


        String path = "/auth_node";
        //添加权限控制
        zooKeeper.addAuthInfo("digest","auth:123".getBytes());
        //创建节点
        zooKeeper.create(path,"add auth".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT,new AuthMyStringCallback(),"create node");


        //当前会话获取
        try {
            //zooKeeper.delete(path,0);

            //查询节点
            byte[] bytes1 = zooKeeper.getData(path,false,null);
            System.out.println("当前会话获取的新建节点数据为："+new String(bytes1));

            //更新节点
            zooKeeper.setData(path,"new data".getBytes(),0);

            byte[] bytesNew = zooKeeper.getData(path,false,null);
            System.out.println("当前会话获取的修改节点后的数据为："+new String(bytesNew));


            String childNode = "/childNode1";
            zooKeeper.create(path+childNode,"childNode1 value".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL,
                    CreateMode.PERSISTENT,new AuthMyStringCallback(),"create childnode");

            System.out.println("====================================================");
            //新创建一个会话
            ZooKeeper zooKeeper1 = ConnectionZk.getZkConnection();
            //获取失败，没有权限 ，报错：KeeperErrorCode = NoAuth for /auth_node
            byte[] bytes2 = zooKeeper1.getData(path,false,null);
            System.out.println("新会话（没有添加权限）获取的新建节点数据为："+new String(bytes2));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除节点
     */
    private static void delNode(){
        zooKeeper = ConnectionZk.getZkConnection();
        try {
            ///auth_node节点是有权限控制的 (可以删除：即使说明，一个有权限的节点，对应删除来说，是可以删除这个节点，但是对应这个
            // 节点的子节点，必须有选项才能删除)
            //zooKeeper.addAuthInfo("digest","auth:123".getBytes());
            zooKeeper.delete("/auth_node",0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


}

class AuthMyStringCallback  implements AsyncCallback.StringCallback{

    @Override
    public void processResult(int status, String path, Object stat, String ctx) {
        System.out.println("异步回调结果：status"+status+";ctx="+ctx+";path="+path+";stat="+stat);
    }
}
