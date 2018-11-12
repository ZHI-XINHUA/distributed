package balance.server;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务器开始
 */
public class ServerRunner {
    /**定义两个服务器**/
    private static final int  SERVER_QTY = 2;
    /**zookeeper服务器**/
    private static final String  ZOOKEEPER_SERVER = "192.168.3.31:2181";
    /**root节点**/
    private static final String  SERVERS_PATH = "/servers";

    public static void main(String[] args) {

       // List<Thread> threadList = new ArrayList<Thread>();

        for(int i=0;i<SERVER_QTY;i++){
            int finalI = i;
            Thread thread =  new Thread(new Runnable() {
                @Override
                public void run() {
                    ServerData serverData = new ServerData();
                    serverData.setHost("127.0.0.1");
                    serverData.setPort(8080+ finalI);
                    serverData.setBalance(0);

                    new ServerImpl(ZOOKEEPER_SERVER,SERVERS_PATH,serverData).bind();
                }
            });

           // threadList.add(thread);
            thread.start();
        }



    }
}
