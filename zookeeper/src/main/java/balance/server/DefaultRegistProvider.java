package balance.server;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

/**
 * 默认服务注册
 */
public class DefaultRegistProvider implements RegistProvicer {
    @Override
    public void regist(Object context) throws Exception {
        ZooKeeperRegistContext registContext = (ZooKeeperRegistContext)context;

        String path = registContext.getPath();
        ZkClient zkClient = registContext.getZkClient();

        try{
            //创建临时节点
            zkClient.createEphemeral(path,registContext.getServerData());
        }catch (ZkNoNodeException e){//节点不存在
            String parentPath = path.substring(0,path.lastIndexOf("/"));
            //创建父节点
            zkClient.createPersistent(parentPath,true);
            //递归调用
            regist(registContext);
        }
    }

    @Override
    public void unRegist(Object context) throws Exception {
        //暂时没用到此方法
    }
}
