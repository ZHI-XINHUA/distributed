package zkbook.zkclient;

import jdk.internal.org.objectweb.asm.TypeReference;
import org.I0Itec.zkclient.IZkConnection;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static javafx.scene.input.KeyCode.T;

/**
 *  zkclient 创建会话
 */
public class ZkConnection {
    /** 服务器连接地址 **/
    private static  String host = "192.168.100.84:2181,192.168.100.51:2181,192.168.100.59:2181";

    /**连接超时时间**/
    static int connectionTimeOut = 50000;

     /**会话超时海事局**/
    static int sessionTimeOut = 5000;



    public static void main(String[] args) {
        ZkClient zkClient = null;

        //1、 默认连接超时时间 30000ms
        //zkClient = new ZkClient(host);

        //2、设置连接超时时间
        //zkClient = new ZkClient(host,connectionTimeOut);

        //3、设置会话超时时间、连接超时时间
        //zkClient = new ZkClient(host,sessionTimeOut,connectionTimeOut);

        //4、设置会话超时时间、连接超时时间、自定义序列化器
        //zkClient = new ZkClient(host,sessionTimeOut,connectionTimeOut,new MyZkSerializer());

        //5、自定义连接（实现IZkConnection）
        zkClient = new ZkClient(new MyIZkConnection(host));



        System.out.println("zkClient="+zkClient.toString());
    }

    public static ZkClient getZkClient(){
        return  new ZkClient(host,sessionTimeOut,connectionTimeOut,new MyZkSerializer());
    }



}
/**
 * 自定义序列化类，使用jackjson序列化
 */
class MyZkSerializer implements ZkSerializer{

    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public byte[] serialize(Object object) throws ZkMarshallingError {
        try {
           return mapper.writeValueAsBytes(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        try {
            return mapper.readValue(bytes,Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

/**
 * 重写IZkConnection，自定义zkclient （一般开发没必要）
 */
class MyIZkConnection implements IZkConnection{

    /**
     *  默认的session超时时间为30 000毫秒
     */
    private static final int DEFAULT_SESSION_TIMEOUT = 30000;
    /**
     * zookeeper 引用
     */
    private ZooKeeper _zk = null;

    /**
     * 定义锁对象
     */
    private Lock _zookeeperLock = new ReentrantLock();

    /**
     * 连接zookeeper字符串
     */
    private final String _servers;

    /**
     * 会话超时时间
     */
    private final int _sessionTimeOut;

    // 构造函数
    public MyIZkConnection(String zkServers) {
        this(zkServers, DEFAULT_SESSION_TIMEOUT);
    }
    public MyIZkConnection(String zkServers, int sessionTimeOut) {
        _servers = zkServers;
        _sessionTimeOut = sessionTimeOut;
    }

    /** 连接zookeeper方法，注册wathcer */
    @Override
    public void connect(Watcher watcher) {
        // 加锁，保证只有一个客户端连接上即可
        _zookeeperLock.lock();
        try {
            if (_zk != null) {
                throw new IllegalStateException("zk client has already been started");
            }
            try {
                // 原生的创建zookeeper连接
                _zk = new ZooKeeper(_servers, _sessionTimeOut, watcher);
            } catch (Exception e) {
                try {
                    throw new Exception("Unable to connect to " + _servers, e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            _zookeeperLock.unlock();
        }
    }
    /**  关闭zookeeper连接  **/
    @Override
    public void close() throws InterruptedException {
        _zookeeperLock.lock();
        try {
            if (_zk != null){
                _zk.close();
                _zk = null;
            }
        } finally {
            _zookeeperLock.unlock();
        }
    }
    // 创建节点
    @Override
    public String create(String path, byte[] data, CreateMode mode) throws KeeperException, InterruptedException {
        return _zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
    }

    @Override
    public String create(String path, byte[] bytes, List<ACL> list, CreateMode mode) throws KeeperException, InterruptedException {
        return _zk.create(path, bytes, list, mode);
    }

    // 删除节点
    @Override
    public void delete(String path) throws InterruptedException, KeeperException {
        _zk.delete(path, -1);
    }

    @Override
    public void delete(String path, int version) throws InterruptedException, KeeperException {
        _zk.delete(path, version);
    }

    // 检查节点是否存在
    @Override
    public boolean exists(String path, boolean watch) throws KeeperException, InterruptedException {
        return _zk.exists(path, watch) != null;
    }
    // 获取当前节点的子节点列表
    @Override
    public List<String> getChildren(final String path, final boolean watch) throws KeeperException, InterruptedException {
        return _zk.getChildren(path, watch);
    }
    // 读取节点数据内容
    @Override
    public byte[] readData(String path, Stat stat, boolean watch) throws KeeperException, InterruptedException {
        return _zk.getData(path, watch, stat);
    }
    // 更新节点数据内容
    public void writeData(String path, byte[] data) throws KeeperException, InterruptedException {
        writeData(path, data, -1);
    }
    // 更新节点数据内容，附带版本信息
    @Override
    public void writeData(String path, byte[] data, int version) throws KeeperException, InterruptedException {
        _zk.setData(path, data, version);
    }

    @Override
    public Stat writeDataReturnStat(String path, byte[] bytes, int version) throws KeeperException, InterruptedException {
       return  _zk.setData(path, bytes, version);
    }

    // 获取zookeeper状态
    @Override
    public ZooKeeper.States getZookeeperState() {
        return _zk != null ? _zk.getState() : null;
    }
    public ZooKeeper getZookeeper() {
        return _zk;
    }
    @Override
    public long getCreateTime(String path) throws KeeperException, InterruptedException {
        Stat stat = _zk.exists(path, false);
        if (stat != null) {
            return stat.getCtime();
        }
        return -1;
    }
    @Override
    public String getServers() {
        return _servers;
    }

    @Override
    public List<OpResult> multi(Iterable<Op> iterable) throws KeeperException, InterruptedException {
        return null;
    }

    @Override
    public void addAuthInfo(String s, byte[] bytes) {

    }

    @Override
    public void setAcl(String s, List<ACL> list, int i) throws KeeperException, InterruptedException {

    }

    @Override
    public Map.Entry<List<ACL>, Stat> getAcl(String s) throws KeeperException, InterruptedException {
        return null;
    }
}
