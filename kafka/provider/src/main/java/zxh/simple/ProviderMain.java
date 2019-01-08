package zxh.simple;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 生产端demo
 */
public class ProviderMain {

    private static final String SERVERS = "192.168.3.31:9092,192.168.3.3.46:9092,192.168.3.118:9092";

    public static void main(String[] args) {
        try {
            initProducer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static  void initProducer() throws InterruptedException {
        Properties provoderConfig = new Properties();
        //该属性指定 broker 的地址清单;集群中建议两个以上，避免一个宕机则不可用
        provoderConfig.put("bootstrap.servers",SERVERS);
        //key序列化类
        provoderConfig.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        //value序列化类
        provoderConfig.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        //消息发送可靠性配置：生产者发送消息到broker，确认方式；（0：producer不会等待broker（leader）发送ack；acks=1：当Leader接收到消息之后发送ack；当所有的follower都同步消息成功后发送ack）
        provoderConfig.put("acks","0");
        //设置生产者内存缓冲区的大小，生产者用它缓冲要发送到服务器的消息
        provoderConfig.put("buffer.memory",32*1024*1024);
        //消息压缩配置，默认不会压缩。（none, gzip, snappy, lz4, zstd）
        provoderConfig.put("compression.type","gzip");
        //发送失败时重发次数
        provoderConfig.put("retries",5);
        //指定了一个批次可以使用的内存大小
        provoderConfig.put("batch.size",16384);
        //指定了生产者在发送批次之前等待更多消息加入批次的时间
        //provoderConfig.put("linger.ms",50);
        //可以是任意的字符串，服务器会用它来识别消息的来橱，还可以用在日志和配额指标里。
        provoderConfig.put("client.id","zxh-proder");
        //参数指定了生产者在收到服务器晌应之前可以发送多少个消息；它的值越高，就会占用越多的内存，不过也会提升吞吐量。 把它设为 1 可以保证消息是按照发送的顺序写入服务器的，即使发生了重试。
        provoderConfig.put("max.in.flight.requests.per.connection",1);
        //控制生产者发送的请求大小;它可以指能发送的单个消息的最大值，也可以指单个请求里所有消息总的大小
        provoderConfig.put("max.request.size",1*1024*1024);
        //指定分区器
        //provoderConfig.put("partitioner.class","zxh.simple.MyPartitioner");

        //创建生成者
        Producer<String,String> producer = new KafkaProducer<String, String>(provoderConfig);

        String topic = "mytopic4";
        for(int j=0;j<3;j++) {
            for (int i = 1; i <= 3; i++) {
                String key = "mykey_"+i;
                if(i==1){
                   // key = "banana";
                }
                ProducerRecord producerRecord = new ProducerRecord<String, String>(topic, key,  "myvalue_"+i);
                //ProducerRecord producerRecord = new ProducerRecord<String, String>(topic, null,  "myvalue_"+i);

                //返送消息记录到指定topic中
                //消息先是被放进缓冲区，然后使用单独的线程发送到服务器端。
                // Future<RecordMetadata> future =  producer.send(producerRecord);
                //同步发送
//            try {
//                //get()等待 Kafka 响应。如果服务器返回错误， get 方怯会抛出异常。如果没有发生错误，我们会得到一个RecordMetadata对象，可以用它获取消息的偏移量。
//                future.get();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }

                //异步发送
                producer.send(producerRecord, new ProviderCallback());

                Thread.sleep(100);
            }
        }

        //关闭
        producer.close();

    }





    /**
     * 异步发送的回调
     */
    private static class ProviderCallback implements Callback{

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            //发送异常，坐相应的处理：如重发等
            if(e!=null){
                e.printStackTrace();

                return;
            }

            //主题
            String topic = recordMetadata.topic();
            //分区
            int partition = recordMetadata.partition();
            //偏移量
            long offset = recordMetadata.offset();



            System.out.println(" top="+topic +"\n partition="+partition +"\n" +
                    " hasOffset="+recordMetadata.hasOffset()+" \n offset="+offset+"\n toString="+ recordMetadata.toString());
            System.out.println();
        }
    }
}
