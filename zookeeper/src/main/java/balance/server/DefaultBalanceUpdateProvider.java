package balance.server;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.data.Stat;

/**
 * 负载均衡计数器实现类
 */
public class DefaultBalanceUpdateProvider implements BalanceUpdateProvider {
    /**服务器路径**/
    private String serverPath;
    /**zk 客户端**/
    private ZkClient zkClient;

    public DefaultBalanceUpdateProvider(String serverPath,ZkClient zkClient){
        super();
        this.serverPath = serverPath;
        this.zkClient = zkClient;
    }

    @Override
    public boolean addBalance(Integer step) {
        Stat stat = new Stat();
        ServerData serverData = zkClient.readData(serverPath,stat);

        //会存在并发，所以循环修改，直接修改成功
        while (true){
            try{
                serverData.setBalance(serverData.getBalance()+step);
                zkClient.writeData(serverPath,serverData,stat.getVersion());
                return true;
            }catch (Exception e ){
                // 忽视
                return false;
            }
        }

    }

    @Override
    public boolean reduceBalance(Integer step) {
        Stat stat = new Stat();
        ServerData serverData = zkClient.readData(serverPath,stat);

        //会存在并发，所以循环修改，直接修改成功
        while (true){
            try{
                Integer newBalance = serverData.getBalance()-step;
                newBalance = newBalance<0 ? 0:newBalance;
                serverData.setBalance(newBalance);
                zkClient.writeData(serverPath,serverData,stat.getVersion());
                return true;
            }catch (Exception e ){
                // 忽视
                return false;
            }
        }
    }
}
