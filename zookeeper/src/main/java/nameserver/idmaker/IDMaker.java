package nameserver.idmaker;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 分布式id生成器
 */
public class IDMaker {
    private ZkClient zkClient;

    private String rootPath;

    private String zkServer;

    private String nodeName;

    private ExecutorService cleanExector = null;

    private  boolean running = false;

    public enum RemoveMethod{
        NONE,IMMEDIATELY,DELAY
    }

    public IDMaker(String zkServer,String rootPath,String nodeName){
        this.zkServer = zkServer;
        this.rootPath = rootPath;
        this.nodeName = nodeName;
    }

    public void start() throws Exception{
        if(running){
            throw new Exception("server has stated...");
        }
        running = true;

        init();
    }

    public void stop() throws Exception {

        if (!running){
            throw new Exception("server has stopped...");
        }

        running = false;

        freeResource();

    }

    private void checkRunning() throws Exception {
        if (!running){
            throw new Exception("请先调用start");
        }


    }

    public void init(){
        zkClient = new ZkClient(zkServer,5000,5000,new BytesPushThroughSerializer());
        cleanExector = Executors.newFixedThreadPool(10);
        zkClient.createPersistent(rootPath,true);
    }

    /**
     * 生成id
     * @param removeMethod
     * @return
     * @throws Exception
     */
    public String generateId(RemoveMethod removeMethod) throws Exception{
        //检查是否启动
        checkRunning();

        String fullNodePath = rootPath.concat("/").concat(nodeName);
        //创建节点
        String outPath = zkClient.createPersistentSequential(fullNodePath,null);

        if(removeMethod.equals(RemoveMethod.IMMEDIATELY)){//立刻删除
            zkClient.delete(outPath);
        }else if (removeMethod.equals(RemoveMethod.DELAY)){//延时删除
            //放到线程池中删除
            cleanExector.execute(new Runnable() {
                @Override
                public void run() {
                    zkClient.delete(outPath);
                }
            });
        }

        //截取节点后面的序号node-0000000000, node-0000000001 -> 0000000000，0000000001
        return  ExtractId(outPath);
    }

    private void freeResource(){

        /*
        shutdown:将线程池状态置为SHUTDOWN,并不会立即停止：

        停止接收外部submit的任务
        内部正在跑的任务和队列里等待的任务，会执行完
        等到第二步完成后，才真正停止
        */
        cleanExector.shutdown();
        try{
            /*
            awaitTermination:当前线程阻塞，直到

            等所有已提交的任务（包括正在跑的和队列中等待的）执行完
                    或者等超时时间到
            或者线程被中断，抛出InterruptedException
            */
            cleanExector.awaitTermination(2, TimeUnit.SECONDS);

        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            cleanExector = null;
        }

        if (zkClient!=null){
            zkClient.close();
            zkClient=null;

        }
    }

    private String ExtractId(String str){
        int index = str.lastIndexOf(nodeName);
        if (index >= 0){
            index+=nodeName.length();
            return index <= str.length()?str.substring(index):"";
        }
        return str;

    }


}
