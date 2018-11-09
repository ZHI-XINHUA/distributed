package masterselect;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xh.zhi on 2018-11-9.
 */
public class WorkServer {
    /**运行标志**/
    private volatile boolean running = false;

    /**master 节点路径**/
    private final String  MASTER_PATH = "/master";

    /**zk客户端**/
    private ZkClient zkClient;

    /**节点数据变化监听器**/
    private IZkDataListener iZkDataListener;

    /**master节点信息**/
    private RunningData masterData;

    /**server 节点信息**/
    private RunningData serverData;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private int delayTime = 5;

    public WorkServer(RunningData runningData,ZkClient zkClient){
        this.serverData = runningData;
        this.zkClient = zkClient;

        iZkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            //节点删除
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

                //如果masterData节点下线（有可能是网络抖动导致以为下线）
                if(masterData!=null && masterData.getName().equals(serverData.getName()) ){
                    //下线的master首先参与获取master竞选，重新成为master
                    takeMaster();
                }else{
                    //不是原本的master竞选，则延迟5秒后再竞选。目的是让网络抖动的导致误以为master下线，竞选其它服务器作为master
                    scheduledExecutorService.schedule(new Runnable() {
                        @Override
                        public void run() {
                            takeMaster();
                        }
                    },delayTime, TimeUnit.SECONDS);
                }
            }
        };

    }

    public void start() throws Exception{
        if(running){
            throw new Exception("server has startup...");
        }
        System.out.println(serverData.getName() +" starting..");
        //设置标志为true
        running = true;

        //订阅
        zkClient.subscribeDataChanges(MASTER_PATH,iZkDataListener);

        //竞选master
        takeMaster();

    }

    public void stop() throws Exception{
        if(!running){
            throw new Exception("server has shutdown...");
        }
        //设置标志为false
        running = false;

        //取消订阅
        zkClient.unsubscribeDataChanges(MASTER_PATH,iZkDataListener);

        //关闭定时任务
        scheduledExecutorService.shutdown();

        //是否资源
        releaseMaster();
    }


    /**
     * 竞选master
     */
    public void takeMaster(){
        if(!running){
            return;
        }

        try{
            //创建master几点
            zkClient.createEphemeral(MASTER_PATH,serverData);
            masterData = serverData;
            System.out.println(serverData.getName()+" is master");

            // 模拟释放master
            scheduledExecutorService.schedule(new Runnable() {
                public void run() {
                    System.out.println("\n模拟释放master:"+masterData.getName());
                    // TODO Auto-generated method stub
                    if (checkMaster()){
                        releaseMaster();
                    }
                }
            }, 5, TimeUnit.SECONDS);

        }catch (ZkNodeExistsException e){//如果master节点存在
            RunningData runningData = zkClient.readData(MASTER_PATH,true);
            //master数据为空，则竞选master
            if(runningData==null){
                takeMaster();
            }else {
                masterData = runningData;
            }
        }

    }

    /**
     * 是否资源
     */
    public void releaseMaster(){
        if(checkMaster()){
            //删除master
            zkClient.delete(MASTER_PATH);
        }

    }

    /**
     * 判断是否master
     * @return
     */
    private boolean checkMaster(){
        RunningData eventData = zkClient.readData(MASTER_PATH);
        masterData = eventData;
        if(masterData.getName().equals(serverData.getName())){
            return true;
        }
        return false;
    }
}
