package zxh.simple;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 一个消费者从一个主题的所有分区或者某个特定的分区读取数据 demo
 */
public class SimpleConsumerMain {
    private static final String SERVERS = "192.168.3.31:9092,192.168.3.3.46:9092,192.168.3.118:9092";

    public static void startConsumer(){
        Properties properties = new Properties();
        //该属性指定 broker 的地址清单;集群中建议两个以上，避免一个宕机则不可用
        properties.put("bootstrap.servers",SERVERS);
        //指定消费组
        properties.put("group.id", "group1");
        //指定key序列化类
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //指定value序列化类
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //创建kafkaConsumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        //主题
        String topic = "mytopic4";
        //分区1
        int partitionNum = 1;
        TopicPartition partition = new TopicPartition(topic,partitionNum);
        List list = Arrays.asList(partition);
        //指定消费topic的那个分区
        consumer.assign(list);

        consumer.seek(partition,90);

        //轮询读取消息
        while (true) {
            /**消费者必须持续对 Kafka 进行轮询，否则会被认为己经死亡 ， 它的分区会被移交给群组里的其他消费者。传给
             poll （） 方法的参数是一个超时时间，用于控制 poll （） 方陆的阻塞时间（在消费者的缓
             冲区里没有可用数据时会发生阻塞）。如果该参数被设为 0, poll （） 会立即返回 ，否则
             它会在指定的毫秒数内一直等待 broker 返回数据。**/
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                String consumerName = Thread.currentThread().getName();
                System.out.printf("partition=%s,offset = %d, key = %s, value = %s\n",
                        record.partition(), record.offset(), record.key(), record.value());


                //处理完当前批次的消息,在轮询更多 的消息之前 , 调用commitSync方位提交当前批 次最新的偏移量。
                consumer.commitSync();
            }
        }

    }

    public static void main(String[] args) {
        startConsumer();
    }
}
