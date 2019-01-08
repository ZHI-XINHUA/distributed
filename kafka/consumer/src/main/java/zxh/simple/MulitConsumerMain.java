package zxh.simple;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * 多个消费者/多个消费者组消费情况 demo
 */
public class MulitConsumerMain {
    private static final String SERVERS = "192.168.3.31:9092,192.168.3.3.46:9092,192.168.3.118:9092";

    private Properties addConfig(String groud){
        Properties consumerConfig = new Properties();
        //该属性指定 broker 的地址清单;集群中建议两个以上，避免一个宕机则不可用
        consumerConfig.put("bootstrap.servers",SERVERS);
        //指定消费组
        consumerConfig.put("group.id", groud);
        //指定key序列化类
        consumerConfig.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //指定value序列化类
        consumerConfig.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //让应用程序决定 何 时提交偏移量。使用 consumer.commitSync() 提交偏移量最简单也最可靠
        consumerConfig.put("auto.commit.offset",false);

        return consumerConfig;
    }


    public   void initConsumer(Properties consumerConfig ,String groud) throws Exception{
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(consumerConfig);

        //订阅主题列表；支持表达式：如 consumer.subscribe("test*")
        List<String> topList= Arrays.asList("mytopic4");
        consumer.subscribe(topList);

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
                    String consumerName = Thread.currentThread().getName();
                    System.out.printf("groud=%s,consumer=%s,partition=%s,offset = %d, key = %s, value = %s\n",
                            groud,consumerName,record.partition(),record.offset(), record.key(), record.value());


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
                    }
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //如果直接关闭消费者，就没有所谓的“下一次提交”了。使用 commitSync方法也会一直重式，直到提交成功或发生无陆恢复的错误。
            consumer.commitSync();
           // consumer.close();
        }

    }

    public static void main(String[] args) {
        final MulitConsumerMain consumer = new MulitConsumerMain();

        try {
            //消费组 groud1
            final String groud1="groud1";
            final Properties properties1 = consumer.addConfig(groud1);
            //模拟多个消费者
            for(int i=0;i<2;i++){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            consumer.initConsumer(properties1,groud1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            //消费组 groud2
            final String groud2="groud2";
            //模拟多个消费者
            final Properties properties2 = consumer.addConfig(groud2);
            for(int i=0;i<2;i++){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            consumer.initConsumer(properties2,groud2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
