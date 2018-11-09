package pullpush;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

/**
 * WorkServer 工作服务器
 */
public class WorkServer {
    /**配置路径，根路径**/
    private String configPath;

    /**工作服务器节点路径**/
    private String serverPath;

    /**工作服务器配置信息**/
    private ServerConfig serverConfig;

    /**工作服务器基本信息**/
    private ServerData serverData;

    /**zk客户端**/
    private ZkClient zkClient;

    /**数据节点监听器**/
    private IZkDataListener zkDataListener;

    /**
     * 构造器
     * @param configPath
     * @param serverPath
     * @param serverConfig
     * @param serverData
     * @param zkClient
     */
    public WorkServer(String configPath, String serverPath, ServerConfig serverConfig, ServerData serverData, ZkClient zkClient){
        this.configPath = configPath;
        this.serverPath = serverPath;
        this.serverConfig = serverConfig;
        this.serverData = serverData;
        this.zkClient = zkClient;

        //实例化监听器
        this.zkDataListener = new IZkDataListener() {
            //监听数据改变
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                //数据改变就更新节点数据
                String json = new String((byte[]) data);
                ServerConfig newServerConfig = JSON.parseObject(json,ServerConfig.class);
                updateConfig(newServerConfig);
                System.out.println("new Work server config is:"+serverConfig.toString());
            }


            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
            }
        };
    }

    /**
     * 启动
     */
    public void start(){
        System.out.println("WorkServer start");
        initRunning();
    }

    /**
     * 停止
     */
    public void stop(){
        System.out.println("WorkServer stop");
        //取消订阅
        zkClient.unsubscribeDataChanges(configPath,zkDataListener);
    }

    public void initRunning(){
        // 1、注册
        registerMe();
        // 2、订阅
        zkClient.subscribeDataChanges(configPath,zkDataListener);
    }

    /**
     * 注册节点
     */
    public void registerMe(){
        String path = serverPath.concat("/").concat(serverData.getAddress());
        try{
            //注册临时节点
            zkClient.createEphemeral(path, JSON.toJSONString(serverData).getBytes());
        }catch (ZkNoNodeException e){//如果没有节点
            //创建父节点
            zkClient.createPersistent(serverPath,true);
            registerMe();
        }
    }

    /**
     * 更新内存服务器配置信息
     * @param serverConfig
     */
    private void updateConfig(ServerConfig serverConfig){
        this.serverConfig = serverConfig;
    }

}
