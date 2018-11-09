package pullpush;
import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

import java.util.List;

/**
 * 管理服务器
 */
public class ManageServer {
    /**配置路径，根路径**/
    private String configPath;

    /**工作服务器节点路径**/
    private String serverPath;

    /**管理服务器的节点路径**/
    private String commandPath;

    /**工作服务器配置信息**/
    private ServerConfig serverConfig;

    /**zk客户端**/
    private ZkClient zkClient;

    /**数据节点commandPath监听器**/
    private IZkDataListener zkDataListener;

    /**子节点监听器**/
    private IZkChildListener zkChildListener;

    /**工作服务器列表**/
    private List<String> workServerList;

    public ManageServer(String configPath, String serverPath, String commandPath, ServerConfig serverConfig, ZkClient zkClient){
        this.configPath = configPath;
        this.serverPath = serverPath;
        this.commandPath = commandPath;
        this.serverConfig = serverConfig;
        this.zkClient = zkClient;

        //实例化节点数据监听器
        this.zkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                String command = new String((byte[])data);
                System.out.println("command :"+command);

                //执行指令
                exeCommand(command);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

            }
        };

        //实例化子节点监听器
        this.zkChildListener = new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                //更新工作服务器列表
                workServerList = currentChilds;

                System.out.println("work server list changed, new list is ");

                //输出WorkServer list
                printWorkServerList();
            }
        };
    }

    public void start() {
        initRunning();
    }

    public void stop() {
        zkClient.unsubscribeDataChanges(commandPath, zkDataListener);
        zkClient.unsubscribeChildChanges(serverPath, zkChildListener);
    }

    /**
     * 初始化运行管理服务
     */
    public void initRunning(){
        //监听commandPath
        zkClient.subscribeDataChanges(commandPath,zkDataListener);
        //监听工作服务器变化
        zkClient.subscribeChildChanges(serverPath,zkChildListener);
    }

    /**
     * 打印工作服务器列表
     */
    public void printWorkServerList(){
        System.out.println(workServerList);
    }

    /**
     * 执行命令方法
     * @param command
     */
    public void exeCommand(String command){
        if("list".equals(command)){
            printWorkServerList();
        }else if("create".equals(command)){
            execCreate();
        }else if("modify".equals(command)){
            execModify();
        }else{
            System.out.println("error command :"+command);
        }
    }

    /**
     * 执行create命令
     */
    public void execCreate(){
        byte[] data = JSON.toJSONString(serverConfig).getBytes();
        if(!zkClient.exists(configPath)){//如果不存在，这新增
            try{
                zkClient.createPersistent(configPath,data);
            }catch (ZkNoNodeException e){//没有节点，这创建父节点
                String parentNode = configPath.substring(0,configPath.lastIndexOf("/"));
                zkClient.createPersistent(parentNode,true);
                execCreate();
            }
        }else{//如果存在，这更新
            zkClient.writeData(configPath, data);
        }
    }

    /**
     * 执行modify命令
     */
    public void execModify(){
        serverConfig.setDbUser(serverConfig.getDbUser()+"_modify");//随便修改
        try {
            zkClient.writeData(configPath, JSON.toJSONString(serverConfig).getBytes());
        } catch (ZkNoNodeException e) {
            execCreate();
        }
    }
}
