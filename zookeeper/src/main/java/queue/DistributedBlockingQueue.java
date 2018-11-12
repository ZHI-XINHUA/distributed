package queue;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 阻塞队列：消费端一直阻塞，直到获取到可消费的对象
 */
public class DistributedBlockingQueue<T> extends DistributedQueue<T> {

    public DistributedBlockingQueue(ZkClient zkClient, String queuePath) {
        super(zkClient, queuePath);
    }

    /**
     * 阻塞消费
     * @return
     */
    public T blockPoll() throws Exception{
      while (true){
          final CountDownLatch latch = new CountDownLatch(1);
          //初始化子节点变化
          final IZkChildListener childListener = new IZkChildListener() {
              @Override
              public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                  //有变化，说明生产端生成了数据
                  latch.countDown();
              }
          };

          //监听子节点变化
          zkClient.subscribeChildChanges(queuePath,childListener);
          try{
              T node = super.poll();
              if(node!=null){
                  return node;
              }else{
                  //没有获取的消费对象，则等待
                  System.out.println("没有获取可消费的对象，等待中......");
                  latch.await();
              }
          }finally {
                //完成后，取消订阅
              zkClient.unsubscribeChildChanges(queuePath,childListener);
          }
      }
    }
}
