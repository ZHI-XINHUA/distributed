package zkbook.curator.use_scene;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.CancelLeadershipException;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import zkbook.curator.CreateSession;

import java.util.concurrent.TimeUnit;

/**
 * Created by xh.zhi on 2018-9-10.
 * Master 选举
 */
public class MasterSelectDemo {

    public static void main(String[] args) throws Exception {
        String path ="/Master_Path";
        //创建会话
        CuratorFramework client = CreateSession.getInstance();
        client.start();

        //master 选举
        LeaderSelector selector = new LeaderSelector(client, path, new LeaderSelectorListener() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("成为Master角色");
                Thread.sleep( 3000 );
                System.out.println( "完成Master操作，释放Master权利" );
            }

            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                System.out.println(connectionState);
                if(connectionState==ConnectionState.LOST || connectionState==ConnectionState.SUSPENDED){
                    throw new CancelLeadershipException();//取消leader 选举
                }

            }
        });



        selector.autoRequeue();
        selector.start();

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}
