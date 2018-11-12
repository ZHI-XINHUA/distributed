package queue;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.concurrent.TimeUnit;

/**
 * Created by xh.zhi on 2018-11-12.
 */
public class TestDistributedBlockingQueue {

    public static void main(String[] args) throws Exception {
        ZkClient zkClient = new ZkClient("192.168.3.31:2181", 5000, 5000, new SerializableSerializer());
        DistributedBlockingQueue<User> queue = new DistributedBlockingQueue<User>(zkClient,"/queue");

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    User user =  new User("999","kobe",40);
                    try {
                        queue.offer(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("生成一个user："+user.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("\n消费开始.....");
        for(int i=0;i<6;i++){
            User user = (User) queue.blockPoll();
            Thread.sleep(1000);
            if(user!=null){
                System.out.println("消费一个user："+user.toString());
            }

        }

        Thread.sleep(Integer.MAX_VALUE);
    }
}
