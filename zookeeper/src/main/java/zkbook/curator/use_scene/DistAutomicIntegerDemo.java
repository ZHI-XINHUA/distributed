package zkbook.curator.use_scene;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.RetryNTimes;
import zkbook.curator.CreateSession;

/**
 * Created by xh.zhi on 2018-9-10.
 * 计数器
 */
public class DistAutomicIntegerDemo {

    public static void main(String[] args) throws Exception {
        String path ="/atomic";

        CuratorFramework client = CreateSession.getInstance();
        client.start();

       DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client,path,new RetryNTimes(1,1000));
        AtomicValue<Integer> rc =  atomicInteger.add(10);
        System.out.println(  rc.postValue()+";"+rc.preValue());
        System.out.println("result="+rc.succeeded());

    }
}
