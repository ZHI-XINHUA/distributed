package queue;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 队列
 */
public class DistributedQueue<T> {

    protected ZkClient zkClient;

    /**队列跟目录**/
    protected String queuePath;

    /**队列的有序节点名称**/
    protected  static final String NODE_NAME = "n_";

    public DistributedQueue(ZkClient zkClient,String queuePath){
        this.zkClient = zkClient;
        this.queuePath = queuePath;
    }

    public int size(){
        return zkClient.getChildren(queuePath).size();
    }

    public boolean isEmpty(){
        return zkClient.getChildren(queuePath).size()==0;
    }

    /**
     * 生产节点
     * @param element
     * @return
     * @throws Exception
     */
    public boolean offer(T element) throws Exception{
        try{
            String path = queuePath.concat("/").concat(NODE_NAME);
            //创建持久有序节点
            zkClient.createPersistentSequential(path,element);
            return true;
        }catch (ZkNoNodeException e){
            //不存在节点则创建父节点,再创建节点
            zkClient.createPersistent(queuePath);
            offer(element);
        }catch (Exception e){
            throw new Exception("创建节点失败！");
        }
        return false;
    }

    /**
     * 消费节点
     * @return
     */
    public T poll(){
        List<String> childList = zkClient.getChildren(queuePath);
        if(childList==null || childList.size()==0){
            return null;
        }

        //排序
        Collections.sort(childList, new Comparator<String>() {
            @Override
            public int compare(String l, String r) {
                return getNodeNumber(l).compareTo(getNodeNumber(r));
            }
        });

        //遍历获取第一个 去消费
        for(String nodeName:childList){
            try{
                String nodeFullPath = queuePath.concat("/").concat(nodeName);
                //获取消费的对象
                T node = (T) zkClient.readData(nodeFullPath);
                //删除节点
                zkClient.delete(nodeFullPath);
                return node;
            }catch (Exception e){//有可能获取第一个消费时候，有其它线程并非消费掉，则需要继续遍历下一个，所以用for循环
                // ignore
            }
        }

        return null;

    }

    private String getNodeNumber(String str) {
        if(str==null || "".equals(str)) {
            return null;
        }
        str = str.replace(NODE_NAME, "");
        return str;

    }
}
