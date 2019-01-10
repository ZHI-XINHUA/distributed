package zxh.provider;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息生产端
 * 发布/订阅（pub/sub） 模式
 */
public class JmsTopicSender {
    private static String brokerURL = "tcp://192.168.1.106:61616";
    public static void main(String[] args) {
        //连接工厂，jms创建连接
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        //jms客户端到JMS provider连接
        Connection connection = null;
        //jms会话
        Session session = null;
        try {
            //创建连接
            connection = connectionFactory.createConnection();
            //启动
            connection.start();

            //创建会话
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            //创建topic
            //destination表示目的地
            Destination destination = session.createTopic("first-topic");

            //创建消息发送者
            MessageProducer producer = session.createProducer(destination);

            //创建发送的消息
            TextMessage textMessage = session.createTextMessage("hello, I' am activemq！");

            //发送消息
            //producer.send(textMessage);
            sendMessage(session,producer);


            //commit消息，消息将添加到队列中待消费者消费
            session.commit();

            session.close();


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(connection!=null) {
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }

    }

    public static void sendMessage(Session session, MessageProducer producer)throws Exception {
        for (int i = 1; i <= 10; i++) {
            TextMessage message = session.createTextMessage("ActiveMq 发送的消息" + i);
            // 发送消息到目的地方
            System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            producer.send(message);
        }
    }
}
