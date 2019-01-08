package zxh.simple;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * 自定义分区器（测试）
 */
public class MyPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List list = cluster.partitionsForTopic(topic);
        int numPatiton = list.size();
        if(((String)key).equals("banana")){
            return 0;//banana的键总是分配0分区上
        }
        //其余的2分区
        return 2;

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
