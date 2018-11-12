package nameserver.idmaker;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public void init(){
        zkClient = new ZkClient(zkServer,5000,5000,new BytesPushThroughSerializer());
        cleanExector = Executors.newFixedThreadPool(10);
        zkClient.createPersistent(rootPath,true);
    }


}
