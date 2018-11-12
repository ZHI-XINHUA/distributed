package distributedlock;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 分布式锁
 */
public class BaseDistributedLock {
    /**zk客户端**/
    private ZkClient zkClient;

    private String basePath;

    private String path;

    private String lockName;

    public BaseDistributedLock(ZkClient zkClient,String basePath, String lockName){
        this.zkClient = zkClient;
        this.basePath = basePath;
        this.path = basePath.concat("/").concat(lockName);
        this.lockName = lockName;
    }



    /**
     * 创建锁节点：临时有序节点
     * @param lockPath
     */
    public void createLockNode(String lockPath){
        zkClient.createEphemeralSequential(lockPath,null);
    }

    /**
     * 释放锁：删除节点
     * @param lockPath
     * @throws Exception
     */
    public void releaseLock(String lockPath){
        zkClient.delete(lockPath);
    }

    private String getLockNodeNumber(String str, String lockName) {
        int index = str.lastIndexOf(lockName);
        if ( index >= 0 ) {
            index += lockName.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;
    }

    /**
     * 获取子节点，并排序
     * @return
     */
    private List<String> getSortedChildren(){
        try{
            List<String> childList = zkClient.getChildren(basePath);
            Collections.sort(childList, new Comparator<String>() {
                @Override
                public int compare(String lLock, String rLock) {
                    System.out.println(getLockNodeNumber(lLock,lockName));
                    return getLockNodeNumber(lLock,lockName).compareTo(getLockNodeNumber(rLock,lockName));
                }
            });
            return childList;
        }catch (ZkNoNodeException e){
            zkClient.createPersistent(basePath,true);
            return getSortedChildren();
        }
    }
}
