package zkbook.curator.tool_class;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.EnsurePath;
import zkbook.curator.CreateSession;

/**
 * Created by xh.zhi on 2018-9-11.
 */
public class EnsurePath_Demo {
    public static void main(String[] args) throws Exception {
        String path ="/zk_path/c1";
        CuratorFramework client = CreateSession.getInstance();
        client.start();

        client.usingNamespace("zk_path");

        EnsurePath ensurePath = new EnsurePath(path);
        ensurePath.ensure(client.getZookeeperClient());
        ensurePath.ensure(client.getZookeeperClient());

        EnsurePath ensurePath2 = client.newNamespaceAwareEnsurePath("/c1");
        ensurePath2.ensure(client.getZookeeperClient());
    }
}
