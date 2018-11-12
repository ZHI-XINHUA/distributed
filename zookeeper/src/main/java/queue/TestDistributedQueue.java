package queue;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * 测试DistributedQueue
 */
public class TestDistributedQueue {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.3.31:2181", 5000, 5000, new SerializableSerializer());
        DistributedQueue queue = new DistributedQueue(zkClient,"/queue");

        //生成
        for(int i=1;i<=5;i++){
            try {
               User user =  new User(i+"","james_"+i,5*i);
               queue.offer(user);
                System.out.println("生成一个user："+user.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n消费开始.....");
        for(int i=0;i<5;i++){
            User user = (User) queue.poll();
            System.out.println("消费一个user："+user.toString());
        }



    }
}
