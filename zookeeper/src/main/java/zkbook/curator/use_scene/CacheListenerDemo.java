package zkbook.curator.use_scene;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import zkbook.curator.CreateSession;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Created by xh.zhi on 2018-9-10.
 *  事件监听：节点和子节点监听
 */
public class CacheListenerDemo {

    public static void main(String[] args) throws Exception {
        //创建会话
        CuratorFramework client = CreateSession.getInstance();
        client.start();

        //nodeCacheListener(client);

        childNodeCacheLinstener(client);
    }


    /**
     * 节点监控demo
     */
    public static void nodeCacheListener(CuratorFramework client) throws Exception {
        final String path ="/cacheNode";

        //监听节点变化：创建和修改。删除监听不了   boolean dataIsCompressed：是否进行压缩
        final NodeCache cache = new NodeCache(client,path,false);
        //true：启动时候读取节点数据
        cache.start(true);
        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println(path+"节点更改:"+new String(cache.getCurrentData().getData()));
            }
        });

        //创建节点
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path,"init".getBytes());

        TimeUnit.SECONDS.sleep(2);

        //更改节点
        client.setData().forPath(path," update value".getBytes());

        TimeUnit.SECONDS.sleep(2);

        //删除节点
        client.delete().guaranteed().forPath(path);

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);

    }

    /**
     * 子节点监控demo
     */
    public static void childNodeCacheLinstener(CuratorFramework client) throws Exception {
        final String path ="/chCacheNode";

        //创建缓存cache  注意path：父节点
        final PathChildrenCache cache = new PathChildrenCache(client,path,true);
        //设置启动策略
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        //注册子节点监听
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                //System.out.println(pathChildrenCacheEvent.getData().getPath()+";"+pathChildrenCacheEvent.getData());
                switch (pathChildrenCacheEvent.getType()){
                    case CHILD_ADDED:
                        System.out.println("添加子节点："+pathChildrenCacheEvent.getData().getPath());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("更新子节点："+pathChildrenCacheEvent.getData().getPath());
                        break;

                    case CHILD_REMOVED:
                        System.out.println("删除子节点："+pathChildrenCacheEvent.getData().getPath());
                        break;
                }

            }
        });

        //创建子节点
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path+"/c1","init value".getBytes());

        TimeUnit.SECONDS.sleep(2);

        //更新
        client.setData().forPath(path+"/c1","update value".getBytes());

        TimeUnit.SECONDS.sleep(2);

        //删除节点
        client.delete().guaranteed().forPath(path+"/c1");

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);

    }
}
