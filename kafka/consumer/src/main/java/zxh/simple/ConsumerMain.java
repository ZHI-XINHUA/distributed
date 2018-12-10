package zxh.simple;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xh.zhi on 2018-12-10.
 */
public class ConsumerMain {
    private static final String SERVERS = "192.168.1.106:9092,192.168.1.108:9092,192.168.1.109:9092";
    private static void initConsumer() throws Exception{
        Properties consumerConfig = new Properties();
        //该属性指定 broker 的地址清单;集群中建议两个以上，避免一个宕机则不可用
        consumerConfig.put("bootstrap.servers",SERVERS);
        //指定消费组
        consumerConfig.put("group.id", "group1");
        //指定key序列化类
        consumerConfig.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //指定value序列化类
        consumerConfig.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //让应用程序决定 何 时提交偏移量。使用 consumer.commitSync() 提交偏移量最简单也最可靠
        consumerConfig.put("auto.commit.offset",false);

        //设定自动提交
        //consumerConfig.put("enable.auto.commit", true);

        //提交时间间隔 默认是5秒
        //consumerConfig.put("auto.commit.interval.ms",3000);

        //other config
        //指定了消费者从服务器获取记录的最小字节数。 broker 在收到消费者的数据请求时，如果可用的数据量小于指定的大小，那么它会等到有足够的可用数据时才把它返回给消费者
        //consumerConfig.put("fetch.min.bytes","1");

        //指定 broker 的等待时间,默认是 500ms
        //consumerConfig.put("fetch.max.wa it.ms",1000);

        //指定了服务器从每个分区里返回给消费者的最大字节数。它的默认值是 lMB;也就是说， consumer.poll方住从每个分区里返回的记录最多不超过 指定的字节
        //consumerConfig.put("max.partition.fetch.bytes",3*1024*1024);

        //指定了消费者在被认为死亡之前可以与服务器断开连接的时间，默认是 3s
        //consumerConfig.put("session.timeout.ms",5000);

        //指定了消费者在读取一个没有偏移量的分区或者偏移量无效的情况下（因消费者长时间失效，包含偏移量的记录已经过时井被删除）该作何处理
        //consumerConfig.put("auto.offset.reset","latest");

        //可以是任意字符串 ， broker 用它来标识从客户端发送过来的消息，通常被用在日志、度量指标和配额里。
        //consumerConfig.put("client.id","client-consumer");

        //分配策略:给定的消费者和主题，决定哪些分区应该被分配给哪个消费者
        //consumerConfig.put("partition.assignment.strategy","org.apache.kafka.clients.consumer.RangeAssignor");

        //属性用于控制单次调用 call （） 方住能够返回的记录数量，可以帮你控制在轮询里需要处理的数据量。
        //consumerConfig.put("max.poll.records",50);

        //设置socket读写数据时TCP缓冲区的大小
        //consumerConfig.put("receive.buffer.bytes",1*1024*1024);
        //consumerConfig.put("send.buffer.bytes",1*1024*1024);

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(consumerConfig);

        //订阅主题列表；支持表达式：如 consumer.subscribe("test*")
        consumer.subscribe(Arrays.asList("my_topic_test","zxh"));

        int minBatchSize = 200;

        try{
            //轮询读取消息
            while (true){
                /**消费者必须持续对 Kafka 进行轮询，否则会被认为己经死亡 ， 它的分区会被移交给群组里的其他消费者。传给
                poll （） 方法的参数是一个超时时间，用于控制 poll （） 方陆的阻塞时间（在消费者的缓
                冲区里没有可用数据时会发生阻塞）。如果该参数被设为 0, poll （） 会立即返回 ，否则
                它会在指定的毫秒数内一直等待 broker 返回数据。**/
                ConsumerRecords<String,String> records =  consumer.poll(100);
                for (ConsumerRecord<String, String> record : records){
                    System.out.printf("offset = %d, key = %s, value = %s\n",
                            record.offset(), record.key(), record.value());

                    //处理完当前批次的消息,在轮询更多 的消息之前 , 调用 COl'll'li.tSync() 方位提交当前批 次最新的偏移量。
                    //consumer.commitSync();
                }

                consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                        if(e!=null){
                            System.err.println("提交失败！");
                            return;
                        }
//                        Set<Map.Entry<TopicPartition, OffsetAndMetadata>> set =  map.entrySet();
//                        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry:set
//                             ) {
//                            TopicPartition topicPartition = entry.getKey();
//                            OffsetAndMetadata offsetAndMetadata = entry.getValue();
//                            System.out.println("topic="+topicPartition.topic() + " offset="+offsetAndMetadata.offset());
//
//                        }
                    }
                });


            }
        }finally {
           // consumer.close();
        }



    }

    public static void main(String[] args) {
        try {
            initConsumer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
